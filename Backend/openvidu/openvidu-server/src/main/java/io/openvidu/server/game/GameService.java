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
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import io.openvidu.client.internal.ProtocolElements;
import io.openvidu.server.core.IdentifierPrefixes;
import io.openvidu.server.core.Participant;
import io.openvidu.server.rpc.RpcNotificationService;

public class GameService {

	static final int PREPAREGAME = 0;
	static final int SELECTGAME = 1;
	static final int STARTGAME = 2;
	static final int FINISHGAME = 3;
	static final int COMPLETEPENALTY = 4;
	static final int VOTELIAR = 5;
	static final int SMILE = 0;
	static final int UPDOWN = 1;
	static final int INITIAL = 2;
	static final int LIAR = 3;
	static final int STRAWBERRY = 4;

	private static final Logger log = LoggerFactory.getLogger(GameService.class);

	static RpcNotificationService rpcNotificationService;

	// Thread는 ConcurrentHashMap으로 관리
	protected ConcurrentHashMap<String, Thread> wordThread = new ConcurrentHashMap<>();

	// < sessionId , <userId,count> >
	protected ConcurrentHashMap<String, TreeMap<String, Integer>> liarCountMap = new ConcurrentHashMap<>();

	// < sessionId , number >
	protected ConcurrentHashMap<String, Integer> upDownNumberMap = new ConcurrentHashMap<>();
	// < sessionId, participantsList >
	protected ConcurrentHashMap<String, ArrayList<Participant>> upDownListMap = new ConcurrentHashMap<>();

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
			Thread alarmThread = new Thread(alarmRunnable);
			wordThread.putIfAbsent(sessionId, alarmThread);
			break;
		case UPDOWN: // UP & DOWN
			int index = data.get("index").getAsInt();
			data.addProperty("participantId", upDownListMap.get(sessionId).get(index).getParticipantPublicId());
			data.addProperty("index", ++index);
			int size = participants.size();
			if (index >= size) {
				index -= size;
			}
			// 숫자 판별
			if (data.has("number")) {
				int number = data.get("number").getAsInt();
				int answer = upDownNumberMap.get(sessionId);
				if (number > answer) {
					data.addProperty("updown", "up");
				} else if (number < answer) {
					data.addProperty("updown", "down");
				} else { // 정답 맞췄을 때
					index++;
					if (index >= size) {
						index -= size;
					}
					data.addProperty("participantId", upDownListMap.get(sessionId).get(index).getParticipantPublicId());
					data.addProperty("gameStatus", 3);
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
			break;
		case LIAR: // 라이어 게임
			LiarGameRunnable liarGameRunnable = new LiarGameRunnable(theme, participants, rnfs);
			Thread liarThread = new Thread(liarGameRunnable);
			wordThread.putIfAbsent(sessionId, liarThread);
			// sessionId, count
			TreeMap<String, Integer> liarMap = new TreeMap<String, Integer>();
			liarMap.put("count", 0);
			for (Participant p : participants) {
				liarMap.put(p.getParticipantPublicId(), 0);
			}
			liarCountMap.put(sessionId, liarMap);
			break;
		case STRAWBERRY: // 딸기 게임
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
		TreeMap<String, Integer> liarMap = liarCountMap.get(sessionId);

		// 라이어 아이디
		String liarId = data.get("liarId").getAsString();

		// 전체 카운트 증가
		liarMap.put("count", liarMap.get("count") + 1);
		// 해당 아이디 카운트 증가
		liarMap.put(voteId, liarMap.get(voteId) + 1);
		// 투표 끝
		if (liarMap.get("count") == participants.size()) {
			// 최다 투표자
			String electId = liarMap.firstKey();

			// 벌칙자 정하기 ( 투표자 == 라이어 )
			String participantId = electId;

			// 라이어가 안걸렸을 때
			if (!liarId.equals(electId)) {
				// 사용자 랜덤 선택
				ArrayList<Participant> pList = new ArrayList<>(participants);
				Collections.shuffle(pList);
				participantId = pList.get(0).getParticipantPublicId();
				// 라이어인지 한번 더 확인
				if (participantId.equals(electId)) {
					participantId = pList.get(1).getParticipantPublicId();
				}
			}

			data.addProperty("voteId", electId);
			data.addProperty("participantId", participantId);
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
