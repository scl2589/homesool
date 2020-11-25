/*
 * (C) Copyright 2017-2020 OpenVidu (https://openvidu.io)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package io.openvidu.server.game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import io.openvidu.client.internal.ProtocolElements;
import io.openvidu.server.core.Participant;
import io.openvidu.server.rpc.RpcNotificationService;

public class GameService {

	static final int PREPAREGAME = 0;
	static final int SELECTGAME = 1;
	static final int STARTGAME = 2;
	static final int FINISHGAME = 3;
	static final int COMPLETEPENALTY = 4;
	static final int VOTELIAR = 5;
	static final int UPDOWN = 1;
	static final int INITIAL = 2;
	static final int LIAR = 3;
	static final int SMILE = 4;
	static final int DRUNKTEST = 5;

	private static final Logger log = LoggerFactory.getLogger(GameService.class);

	static RpcNotificationService rpcNotificationService;
	static InitialGameUtil initialGameUtil = new InitialGameUtil();
	
	// Thread는 ConcurrentHashMap으로 관리
	protected ConcurrentHashMap<String, Thread> wordThread = new ConcurrentHashMap<>();
	// < sessionId , <userId,count> >
	protected ConcurrentHashMap<String, Map<String, Integer>> liarCountMap = new ConcurrentHashMap<>();
	// < sessionId , number >
	protected ConcurrentHashMap<String, Integer> upDownNumberMap = new ConcurrentHashMap<>();
	// < sessionId, participantsList >
	protected ConcurrentHashMap<String, ArrayList<Participant>> upDownListMap = new ConcurrentHashMap<>();
	// < sessionId, initialWord >
	protected ConcurrentHashMap<String, String> initialWordMap = new ConcurrentHashMap<>();
	// < sessionId, String >
	protected ConcurrentHashMap<String, HashSet<String>> initialAnswerMap = new ConcurrentHashMap<>();
	// < sessionId, Set<Participant> >
	protected ConcurrentHashMap<String, HashSet<Participant>> initialAnswerUserMap = new ConcurrentHashMap<>();
	// < sessionId, sentence >
	protected ConcurrentHashMap<String, String> drunkTestMap = new ConcurrentHashMap<>();

	public void controlGame(Participant participant, JsonObject message, Set<Participant> participants,
			RpcNotificationService rnfs) {
		rpcNotificationService = rnfs;
		JsonObject params = new JsonObject();

		// 요청 보낸 사람 ID 저장
		if (participant != null) {
			params.addProperty(ProtocolElements.PARTICIPANTSENDMESSAGE_FROM_PARAM,
					participant.getParticipantPublicId());
		}
		// 타입 저장
		if (message.has("type")) {
			params.addProperty(ProtocolElements.PARTICIPANTSENDMESSAGE_TYPE_PARAM, message.get("type").getAsString());
		}
		// data 파싱
		String dataString = message.get("data").getAsString();
		JsonObject data = (JsonObject) JsonParser.parseString(dataString);

		// 게임 상태에 따라 분기
		int gameStatus = data.get("gameStatus").getAsInt();

		// 게임 상태 추가, 벌칙 완료일때만 4 -> 0 으로
		data.addProperty("gameStatus", Integer.toString(gameStatus));

		switch (gameStatus) {
		case PREPAREGAME: // 게임 준비
			prepareGame(participant, message, participants, params, data);
			return;
		case SELECTGAME: // 게임 선택
			selectGame(participant, message, participants, params, data);
			return;
		case STARTGAME: // 게임 진행
			startGame(participant, message, participants, params, data, rnfs);
			return;
		case FINISHGAME: // 게임 종료
			finishGame(participant, message, participants, params, data);
			return;
		case COMPLETEPENALTY: // 벌칙 종료
			completePenalty(participant, message, participants, params, data);
			return;
		case VOTELIAR:
			voteLiar(participant, message, participants, params, data);
			return;
		}
	}

	// 게임 준비
	// 특정 사용자가 게임을 고르는 동안 다른 사용자들은 '게임을 선택중입니다' 화면이 보여야 함
	private void prepareGame(Participant participant, JsonObject message, Set<Participant> participants,
			JsonObject params, JsonObject data) {
		log.info("PrepareGame is called by {}", participant.getParticipantPublicId());

		params.add("data", data);
		// 브로드 캐스팅
		for (Participant p : participants) {
			rpcNotificationService.sendNotification(p.getParticipantPrivateId(),
					ProtocolElements.PARTICIPANTSENDMESSAGE_METHOD, params);
		}
	}

	// 게임 선택
	private void selectGame(Participant participant, JsonObject message, Set<Participant> participants,
			JsonObject params, JsonObject data) {
		log.info("selectGame is called by {}", participant.getParticipantPublicId());

		// 업다운 게임이면 랜덤숫자, 순서 생성
		int gameId = data.get("gameId").getAsInt();
		if (gameId == UPDOWN) {
			ArrayList<Participant> pList = new ArrayList<Participant>(participants);
			Collections.shuffle(pList);
			upDownNumberMap.put(message.get("sessionId").getAsString(), (int) (Math.random() * 100) + 1);
			upDownListMap.put(message.get("sessionId").getAsString(), pList);
			data.addProperty("index", 1);
			data.addProperty("participantPublicId", pList.get(0).getParticipantPublicId());
		} else if (gameId == INITIAL) {
			String[] initial = { "ㄱ", "ㄴ", "ㄷ", "ㄹ", "ㅁ", "ㅂ", "ㅅ", "ㅇ", "ㅈ", "ㅊ", "ㅋ", "ㅌ", "ㅍ", "ㅎ" };
			// 랜덤 초성 선택
			ArrayList<String> randomWords = new ArrayList<String>(Arrays.asList(initial));
			Collections.shuffle(randomWords);
			// 첫째자리
			String initialword = randomWords.get(0);
			Collections.shuffle(randomWords);
			// 둘째자리
			initialword += randomWords.get(0);

			data.addProperty("initialWord", initialword);
			// 초성 저장
			initialWordMap.put(message.get("sessionId").getAsString(), initialword);
			// 정답 단어 목록
			initialAnswerMap.put(message.get("sessionId").getAsString(), new HashSet<String>());
			// 정답 유저 목록
			HashSet<Participant> pSet = new HashSet<Participant>(participants);
			initialAnswerUserMap.put(message.get("sessionId").getAsString(), pSet);

		}
		params.add("data", data);
		// 브로드 캐스팅
		for (Participant p : participants) {
			rpcNotificationService.sendNotification(p.getParticipantPrivateId(),
					ProtocolElements.PARTICIPANTSENDMESSAGE_METHOD, params);
		}
	}

	// 게임 시작
	private void startGame(Participant participant, JsonObject message, Set<Participant> participants,
			JsonObject params, JsonObject data, RpcNotificationService rnfs) {
		log.info("startGame is called by {}", participant.getParticipantPublicId());
		int gameId = data.get("gameId").getAsInt();
		String sessionId = message.get("sessionId").getAsString();
		// 테마 가져오기
		String theme = null;
		if (data.has("theme"))
			theme = data.get("theme").getAsString();

		switch (gameId) {
		case SMILE: // 웃으면 술이와요
			// 스레드 시작
			SmileRunnable alarmRunnable = new SmileRunnable(theme, participants, rnfs);
			Thread smileThread = new Thread(alarmRunnable);
			smileThread.start();
			wordThread.putIfAbsent(sessionId, smileThread);
			break;
		case UPDOWN: // UP & DOWN
			int index = data.get("index").getAsInt();
			int size = participants.size();
			if (index >= size) {
				index -= size;
			}
			data.addProperty("participantPublicId", upDownListMap.get(sessionId).get(index).getParticipantPublicId());
			data.addProperty("index", ++index);
			
			
			// 숫자 판별
			if (data.has("number")) {
				int number = data.get("number").getAsInt();
				int answer = upDownNumberMap.get(sessionId);
				if (number > answer) {
					data.addProperty("updown", "down");
				} else if (number < answer) {
					data.addProperty("updown", "up");
				} else { // 정답 맞췄을 때
					if (index >= size) {
						index -= size;
					}
					data.addProperty("gameStatus", 3);

					upDownNumberMap.remove(sessionId);
					upDownListMap.remove(sessionId);
				}
			}
			
			// 게임 순서와 데이터 보내주기
			params.add("data", data);
			for (Participant p : participants) {
				rpcNotificationService.sendNotification(p.getParticipantPrivateId(),
						ProtocolElements.PARTICIPANTSENDMESSAGE_METHOD, params);
			}
			break;
		case INITIAL: // 자음 퀴즈
			String word = data.get("word").getAsString();
			String ansInitWord = initialWordMap.get(sessionId);
			String result = "";
			int isCorrect = 1;
			// 길이 검증
			if (word.length() == 2) {
				// 초성 검증
				String initialWord = initialGameUtil.Direct(word.charAt(0));
				initialWord += initialGameUtil.Direct(word.charAt(1));
				if (initialWord.equals(ansInitWord)) {
					// 중복 검증
					if (!initialAnswerMap.get(sessionId).contains(word)) {
						// 사전 검증
						if (initialGameUtil.searchWord(word)) {
							isCorrect = 2;
							initialAnswerMap.get(sessionId).add(word);
							initialAnswerUserMap.get(sessionId).remove(participant);
						}
						else {
							result = "사전에 없는 단어입니다";
						}
					}
					else {
						result = "중복된 단어입니다";
					}
				}
				else {
					result = "초성이 다릅니다";
				}
			}
			else {
				result = "글자수를 맞춰주세요";
			}
			data.addProperty("result", result);
			data.addProperty("isCorrect", isCorrect);
			if (initialAnswerUserMap.get(sessionId).size() > 1) {
				params.add("data", data);
				for (Participant p : participants) {
					rpcNotificationService.sendNotification(p.getParticipantPrivateId(),
							ProtocolElements.PARTICIPANTSENDMESSAGE_METHOD, params);
				}
			}
			// 꼴지 정해짐
			else {
				String participantPublicId = null;
				for (Participant p : initialAnswerUserMap.get(sessionId)) {
					participantPublicId = p.getParticipantPublicId();
				}
				data.addProperty("participantPublicId", participantPublicId);
				data.addProperty("gameStatus", 3);
				params.add("data", data);

				initialWordMap.remove(sessionId);
				initialAnswerMap.remove(sessionId);
				initialAnswerUserMap.remove(sessionId);

				for (Participant p : participants) {
					rpcNotificationService.sendNotification(p.getParticipantPrivateId(),
							ProtocolElements.PARTICIPANTSENDMESSAGE_METHOD, params);
				}
			}
			break;
		case LIAR: // 라이어 게임
			LiarGameRunnable liarGameRunnable = new LiarGameRunnable(theme, participants, rnfs);
			Thread liarThread = new Thread(liarGameRunnable);
			liarThread.start();
			wordThread.putIfAbsent(sessionId, liarThread);
			// sessionId, count
			Map<String, Integer> liarMap = new HashMap<String, Integer>();
			liarMap.put("count", 0);
			for (Participant p : participants) {
				liarMap.put(p.getParticipantPublicId(), 0);
			}
			liarCountMap.put(sessionId, liarMap);
			break;
		case DRUNKTEST: // 나안취했어
			// 처음 요청
			if(!data.has("sentence")) {
				String sentence = DrunkTestUtil.sentences[(int) (Math.random() * DrunkTestUtil.sentences.length)];
				data.addProperty("sentence", sentence);
				// 띄어쓰기 제거 후 저장
				drunkTestMap.put(message.get("sessionId").getAsString(), sentence.replaceAll(" ", ""));
			}
			// 정답 검증 요청
			else {
				String answer = drunkTestMap.get(sessionId);
				String sentence = data.get("sentence").getAsString();
				data.addProperty("sentence", sentence);
				// 공백 제거 후 비교
				String drunk;
				// 통과
				if (answer.equals(sentence.replaceAll("\\ |\\.|\\,|\\?", ""))) {
					drunk = "1";
				// 실패
				} else {
					drunk = "2";
				}
				data.addProperty("drunk", drunk);
				data.addProperty("gameStatus", 3);
				drunkTestMap.remove(sessionId);
			}
			params.add("data", data);
			for (Participant p : participants) {
				rpcNotificationService.sendNotification(p.getParticipantPrivateId(),
						ProtocolElements.PARTICIPANTSENDMESSAGE_METHOD, params);
			}
			break;
		}
	}

	private void finishGame(Participant participant, JsonObject message, Set<Participant> participants,
			JsonObject params, JsonObject data) {
		log.info("finishGame is called by {}", participant.getParticipantPublicId());
		// sessionId
		String sessionId = message.get("sessionId").getAsString();
		Thread now = wordThread.get(sessionId);
		wordThread.remove(sessionId);

		if (now != null) {
			now.interrupt();
		}
		data.addProperty("gameStatus", 3);
		params.add("data", data);
		for (Participant p : participants) {
			rpcNotificationService.sendNotification(p.getParticipantPrivateId(),
					ProtocolElements.PARTICIPANTSENDMESSAGE_METHOD, params);
		}
	}

	private void completePenalty(Participant participant, JsonObject message, Set<Participant> participants,
			JsonObject params, JsonObject data) {
		log.info("completePenalty is called by {}", participant.getParticipantPublicId());

		data.addProperty("gameStatus", 0);
		params.add("data", data);
		for (Participant p : participants) {
			rpcNotificationService.sendNotification(p.getParticipantPrivateId(),
					ProtocolElements.PARTICIPANTSENDMESSAGE_METHOD, params);
		}
	}

	// 라이어게임 투표
	private void voteLiar(Participant participant, JsonObject message, Set<Participant> participants, JsonObject params,
			JsonObject data) {
		log.info("voteLiar is called by {}", participant.getParticipantPublicId());

		data.addProperty("gameStatus", 3);

		// 세션 아이디
		String sessionId = message.get("sessionId").getAsString();
		// 지목당한 아이디
		String voteId = data.get("voteId").getAsString();
		Map<String, Integer> liarMap = liarCountMap.get(sessionId);

		// 라이어 아이디
		String liarId = data.get("liarId").getAsString();

		// 전체 카운트 증가
		liarMap.put("count", liarMap.get("count") + 1);
		// 해당 아이디 카운트 증가
		liarMap.put(voteId, liarMap.get(voteId) + 1);
		// 투표 끝
		if (liarMap.get("count") == participants.size()) {
			
			liarMap.remove("count");	//없애주지 않으면 무조건 count를 반환
			// value 내림차순으로 정렬하고, value가 같으면 key 오름차순으로 정렬
	        List<Map.Entry<String, Integer>> list = new LinkedList<>(liarMap.entrySet());
	        
	        Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
	            @Override
	            public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
	                int comparision = (o1.getValue() - o2.getValue()) * -1;
	                return comparision == 0 ? o1.getKey().compareTo(o2.getKey()) : comparision;
	            }
	        });
	        
			// 최다 투표자
			String electId = list.get(0).getKey();
			// 벌칙자 정하기 ( 투표자 == 라이어 )
			String participantPublicId = electId;

			// 라이어가 안걸렸을 때
			if (!liarId.equals(electId)) {
				// 사용자 랜덤 선택
				ArrayList<Participant> pList = new ArrayList<>(participants);
				Collections.shuffle(pList);
				participantPublicId = pList.get(0).getParticipantPublicId();
				// 라이어인지 한번 더 확인
				if (participantPublicId.equals(liarId)) {
					participantPublicId = pList.get(1).getParticipantPublicId();
				}
			}
			
			data.addProperty("voteId", electId);
			data.addProperty("participantPublicId", participantPublicId);
			params.add("data", data);

			// 게임 세션 제거
			Thread now = wordThread.get(sessionId);
			liarCountMap.remove(sessionId);
			wordThread.remove(sessionId);

			if (now != null) {
				now.interrupt();
			}

			for (Participant p : participants) {
				rpcNotificationService.sendNotification(p.getParticipantPrivateId(),
						ProtocolElements.PARTICIPANTSENDMESSAGE_METHOD, params);
			}
		}
	}
}
