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
	static final int SMILE = 0;
	static final int UPDOWN = 1;
	static final int INITIAL = 2;
	static final int LIAR = 3;
	static final int STRAWBERRY = 4;
	static String[] smileWords = { "a", "b", "c", "d", "e", "f", "g", "h", "i" };
	static int TESTVALUE = 1;

	private static final Logger log = LoggerFactory.getLogger(GameService.class);

	static RpcNotificationService rpcNotificationService;
	
	//Thread는 ConcurrentHashMap으로 관리
	private AlarmRunnable alarmRunnable = null;
	protected ConcurrentHashMap<String, Thread> WordThread = new ConcurrentHashMap<>();

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
			startGame(participant, message, participants, params, data);
			return;
		case FINISHGAME: // 게임 종료
			finishGame(participant, message, participants, params, data);
			return;
		case COMPLETEPENALTY: // 벌칙 종료
			completePenalty(participant, message, participants, params, data);
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
		
		params.add("data", data);
		// 브로드 캐스팅
		for (Participant p : participants) {
			rpcNotificationService.sendNotification(p.getParticipantPrivateId(),
					ProtocolElements.PARTICIPANTSENDMESSAGE_METHOD, params);
		}
	}

	// 게임 시작
	private void startGame(Participant participant, JsonObject message, Set<Participant> participants,
			JsonObject params, JsonObject data) {
		log.info("startGame is called by {}", participant.getParticipantPublicId());
		int gameId = data.get("gameId").getAsInt();
		String sessionId = message.get("sessionId").getAsString();
		switch (gameId) {
		case SMILE: // 웃으면 술이와요
			
			System.out.println("message :" + message);
			
			alarmRunnable = new AlarmRunnable();
			alarmThread = new Thread(alarmRunnable);
			WordThread.putIfAbsent(sessionId, alarmThread);
			alarmThread.start();
			//asyncTaskService.SendGameMessgage();
			//새로운 Service를 사용하는 것이 아니라 현 시점에서 새로운 스레드 생성??
			
			/*
			// 랜덤 단어 선택
			ArrayList<String> randomWords = new ArrayList<>(Arrays.asList(smileWords));
			Collections.shuffle(randomWords);
			// 게임 진행할 플레이어
			ArrayList<Participant> randomParticipants = new ArrayList<>(participants);
			Collections.shuffle(randomParticipants);
 
			int wIdx = 0;
			int pIdx = 0;
			int wMax = randomWords.size();
			int pMax = randomParticipants.size();
			int tIdx = 0;
			while (true) {
				if (wIdx >= wMax)
					wIdx -= wMax;
				if (pIdx >= pMax)
					pIdx -= pMax;
				data.addProperty("word", randomWords.get(wIdx));
				data.addProperty("player", randomParticipants.get(pIdx).getParticipantPublicId());
				params.add("data", data);
				for (Participant p : participants) {
					rpcNotificationService.sendNotification(p.getParticipantPrivateId(),
							ProtocolElements.PARTICIPANTSENDMESSAGE_METHOD, params);
				}
				wIdx++;
				pIdx++;
				tIdx++;
				// 게임시간 3초씩 주기
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				if (tIdx == 10) {
					//finishGame(participant, message, participants, params, data);
					//break;
				}
			}*/
		case UPDOWN: // UP & DOWN
			break;
		case INITIAL: // 자음 퀴즈
			break;
		case LIAR: // 라이어 게임
			break;
		case STRAWBERRY: // 딸기 게임
			break;
		}
	}

	private void finishGame(Participant participant, JsonObject message, Set<Participant> participants,
			JsonObject params, JsonObject data) {
		log.info("finishGame is called by {}", participant.getParticipantPublicId());
		//sessionId
		String sessionId = message.get("sessionId").getAsString();
		Thread now = WordThread.get(sessionId);
		WordThread.remove(sessionId);
		
		if(now != null) {
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

}
