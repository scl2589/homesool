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
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.kurento.jsonrpc.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonObject;

import io.openvidu.client.internal.ProtocolElements;
import io.openvidu.server.core.IdentifierPrefixes;
import io.openvidu.server.core.Participant;
import io.openvidu.server.rpc.RpcConnection;

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

	private static final Logger log = LoggerFactory.getLogger(GameService.class);

	private ConcurrentMap<String, RpcConnection> rpcConnections = new ConcurrentHashMap<>();

	public void controlGame(Participant participant, JsonObject message, Set<Participant> participants) {

		JsonObject params = new JsonObject();

		// 요청 보낸 사람 ID 저장
		if (participant != null) {
			params.addProperty(ProtocolElements.PARTICIPANTSENDMESSAGE_FROM_PARAM,
					participant.getParticipantPublicId());
		}

		// 게임 상태에 따라 분기
		int gameStatus = message.get("gameStatus").getAsInt();
		
		// 게임 상태 추가, 벌칙 완료일때만 4 -> 0 으로
		params.addProperty("gameStatus", Integer.toString(gameStatus));

		switch (gameStatus) {
		case PREPAREGAME: // 게임 준비
			prepareGame(participant, message, participants, params);
			return;
		case SELECTGAME: // 게임 선택
			selectGame(participant, message, participants, params);
			return;
		case STARTGAME: // 게임 진행
			startGame(participant, message, participants, params);
			return;
		case FINISHGAME: // 게임 종료
			finishGame(participant, message, participants, params);
			return;
		case COMPLETEPENALTY: // 벌칙 종료
			completePenalty(participant, message, participants, params);
			return;
		}
	}

	// 게임 준비
	private void prepareGame(Participant participant, JsonObject message, Set<Participant> participants,
			JsonObject params) {
		// 특정 사용자가 게임을 고르는 동안 다른 사용자들은 '게임을 선택중입니다' 화면이 보여야 함
		params.addProperty("gameStatus", message.get("gameStatus").toString());

		// 브로드 캐스팅
		for (Participant p : participants) {
			sendNotification(p.getParticipantPrivateId(), ProtocolElements.PARTICIPANTSENDMESSAGE_METHOD, params);
		}
	}

	// 게임 선택
	private void selectGame(Participant participant, JsonObject message, Set<Participant> participants,
			JsonObject params) {
		params.addProperty("gameId", message.get("gameId").toString());
		params.addProperty("paneltyId", message.get("paneltyId").toString());

		// 브로드 캐스팅
		for (Participant p : participants) {
			sendNotification(p.getParticipantPrivateId(), ProtocolElements.PARTICIPANTSENDMESSAGE_METHOD, params);
		}
	}

	// 게임 시작
	private void startGame(Participant participant, JsonObject message, Set<Participant> participants,
			JsonObject params) {
		int gameId = message.get("gameId").getAsInt();
		switch (gameId) {
		case SMILE: // 웃으면 술이와요
			params.addProperty("theme", message.get("theme").getAsString());

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
				params.addProperty("word", randomWords.get(wIdx));
				params.addProperty("player", randomParticipants.get(pIdx).getParticipantPublicId());

				for (Participant p : participants) {
					sendNotification(p.getParticipantPrivateId(), ProtocolElements.PARTICIPANTSENDMESSAGE_METHOD,
							params);
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
//					finishGame(participant, message, participants, params);
					break;
				}
			}
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
			JsonObject params) {
	}

	private void completePenalty(Participant participant, JsonObject message, Set<Participant> participants,
			JsonObject params) {
		params.addProperty("gameStatus", "0");
		for (Participant p : participants) {
			sendNotification(p.getParticipantPrivateId(), ProtocolElements.PARTICIPANTSENDMESSAGE_METHOD,
					params);
		}
	}

	public void sendNotification(final String participantPrivateId, final String method, final Object params) {
		RpcConnection rpcSession = rpcConnections.get(participantPrivateId);
		if (rpcSession == null || rpcSession.getSession() == null) {
			if (!isIpcamParticipant(participantPrivateId)) {
				log.error("No rpc session found for private id {}, unable to send notification {}: {}",
						participantPrivateId, method, params);
			}
			return;
		}
		Session s = rpcSession.getSession();

		try {
			s.sendNotification(method, params);
		} catch (Exception e) {
			log.error("Exception sending notification '{}': {} to participant with private id {}", method, params,
					participantPrivateId, e);
		}
	}

	private boolean isIpcamParticipant(String participantPrivateId) {
		return participantPrivateId.startsWith(IdentifierPrefixes.IPCAM_ID);
	}

}
