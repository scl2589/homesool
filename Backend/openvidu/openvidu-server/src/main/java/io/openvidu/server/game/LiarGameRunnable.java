package io.openvidu.server.game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

import com.google.gson.JsonObject;

import io.openvidu.client.internal.ProtocolElements;
import io.openvidu.server.core.Participant;
import io.openvidu.server.rpc.RpcNotificationService;

public class LiarGameRunnable implements Runnable {
	public boolean running = true;

	static String[] hotfruits = { "가시아노나", "감나무", "거버너자두", "검은감나무", "구아바", "귤", "그물아노나", "금귤", "기적의열매", "나무토마토",
			"날개시계초", "노란감나무", "노란망고스틴", "노란사포테", "노란용과", "단시계초", "대추야자", "두리안", "딸기구아바", "라임", "람부탄", "랑삿", "레몬", "로젤",
			"리치", "마닐라타마린", "마카다미아", "마프랑", "말라카사과", "말레이시아포도", "망고", "망고스틴", "매마등", "멕시코사과", "멜론", "모과사과", "무화과",
			"물사과", "바나나", "바나나시계초", "버마포도", "별과일", "붉은스폰디아", "붉은용과", "브라질넛", "브라질체리", "브라질포도", "비낭야자", "비늘야자", "비림비",
			"비파", "빵나무", "뿔참외", "사포딜라", "산톨", "산파파야", "선인장", "수리남체리", "수박", "스폰디아", "시계초", "시트론", "아노나", "아마존사포테",
			"아보카도", "아세로라", "아키이", "아테모야", "안데스사포테", "오렌지", "올리브", "용과", "용안", "인도대추", "인도오디", "잉가", "자바사과", "작은빵나무",
			"카란다", "카카오", "캐슈", "커피", "코코넛", "쿠미니자두", "큰귤", "큰꽃카란다", "큰빵나무", "큰사포테", "큰시계초", "타마린", "털감나무", "토마토",
			"파나마사과", "파인애플", "파파야", "페루꽈리", "페타이콩", "페피노", "피스타치오", "황피" };

	static String[] vegi = { "스타후르츠", "블림빙", "잠부", "오미자", "끌렝깽", "사워", "감자", "고구마", "깻잎", "당근", "도라지", "대파", "마늘", "무",
			"미나리", "버섯", "배추", "부추", "고추", "브로콜리", "생강", "시금치", "연근", "우엉", "양파", "양배추", "호박", "깻잎", "옥수수", "청경채", "배추",
			"시금치", "부추", "가지", "파란고추", "실파", "대파", "미나리", "애호박", "단호박", "오이", "당근", "감자", "고구마", "버섯", "양송이", "느타리버섯",
			"표고버섯", "말린버섯", "무", "단무지", "피클", "무청", "상추", "양배추", "양상추", "바질", "마늘", "생강", "순무", "브로콜리", "인삼", "쑥갓",
			"피망" };
	
	static String[] animal = {"가시두더지","개","개미핥기","개코원숭이","고라니","고래","고릴라","고슴도치","고양이","곰",
			"기린","나무늘보","낙타","날다람쥐","너구리","늑대","다람쥐","당나귀","돌고래","돼지",
			"두더지","말","멧돼지","물개","바다표범","박쥐","반달가슴곰","벵골호랑이","북극곰","북극여우",
			"불독","사막여우","사슴","사자","생쥐","수달","스컹크","알파카","여우","염소",
			"오랑우탄","오소리","원숭이","족제비","청설모","치와와","치타","침팬지","캥거루","코끼리",
			"코뿔소","코알라","토끼","펭귄","표범","호랑이"
	};
	
	static String[] country = {"가나","과테말라","그리스","나이지리아","남아프리카 공화국","네덜란드","네팔","노르웨이","뉴질랜드","대한민국",
			"덴마크","독일","라오스","레바논","룩셈부르크","말레이시아","멕시코","몰디브","몽골","미국",
			"방글라데시","베네수엘라","베트남","벨기에","북한","브라질","사우디아라비아","스리랑카","스웨덴",
			"스위스","스페인","싱가포르","아랍에미리트","아르헨티나","아프가니스탄","영국","오스트리아","우즈베키스탄",
			"이라크","이스라엘","이집트","이탈리아","인도","일본","자메이카","중국","체코","캄보디아",
			"태국","터키","포르투갈","폴란드","프랑스","핀란드","필리핀","헝가리","호주"
	};
	static String[] food = { "김치", "푸아그라", "파스타", "스시", "똠양꿍", "나시고랭", "탄두리치킨", "퐁듀", "케밥", "와플", "타코", "딤섬",
			"피쉬앤칩스", "쌀국수", "불고기", "비빔밥", "냉면", "떡볶이", "감자탕", "삼계탕", "전", "김치찌개", "카레", "돈까스", "우동", "타코야키",
			"만두", "훠궈", "마라탕", "양꼬치", "취두부", "팟타이", "똠양꿍", "스테이크", "바게트", "햄버거", "핫도그", "피자", "스파게티"
	};
	static String[] movie = {"어벤져스", "아바타", "타이타닉", "스타워즈", "쥬라기 월드", "분노의 질주", "겨울왕국", "해리 포터", "미녀와 야수", "인크레더블",
			"아이언맨", "미니언즈", "캡틴 아메리카", "아쿠아맨", "트랜스포머", "다크 나이트 라이즈", "토이 스토리", "캐리비안의 해적", "이상한 나라의 앨리스", "라이온 킹",
			"니모를 찾아서", "바람과 함께 사라지다", "사운드 오브 뮤직", "ET", "명량", "극한직업", "신과함께", "국제시장", "베테랑", "도둑들", "7번방의 선물", "암살",
			"알라딘", "광해, 왕이 된 남자", "부산행", "해운대", "괴물", "왕의 남자", "인터스텔라", "인셉션", "기생충", "보헤미안 랩소디", "검사외전", "엑시트", "내부자들",
			"국가대표", "디워", "히말라야", "밀정", "써니", "스파이더맨", "터널"
	};

	private String theme;
	private Set<Participant> participants;
	private Map<String,String[]> wordMap = Map.of(
			"열대과일",hotfruits,
			"야채",vegi,
			"동물",animal,
			"나라",country,
			"음식",food,
			"영화",movie
			);
	static RpcNotificationService rpcNotificationService;

	public LiarGameRunnable(String theme, Set<Participant> participants, RpcNotificationService rnfs) {
		this.theme = theme;
		this.participants = participants;
		this.rpcNotificationService = rnfs;
	}

	public void terminate() {
		running = false;
	}

	@Override
	public void run() {
		ArrayList<String> randomWords = new ArrayList<>(Arrays.asList(wordMap.get(theme)));
		Collections.shuffle(randomWords);
		ArrayList<Participant> randomParticipants = new ArrayList<>(participants);
		Collections.shuffle(randomParticipants);

		// 단어와 라이어가 누구인지 알려줌
		try {
			JsonObject data = new JsonObject();
			JsonObject params = new JsonObject();
			
			//signal:game parameter 추가
			String temp = "signal:game";
	        params.addProperty("type", temp);
	         
			data.addProperty("gameStatus", 2);
			data.addProperty("word", randomWords.get(0));
			data.addProperty("liarId", randomParticipants.get(0).getParticipantPublicId());
			data.addProperty("turn", 0);

			params.add("data", data);
			for (Participant p : participants) {
				rpcNotificationService.sendNotification(p.getParticipantPrivateId(),
						ProtocolElements.PARTICIPANTSENDMESSAGE_METHOD, params);
			}
			// 5초 동안 단어 확인 후 게임 시작
			Thread.sleep(5000);
			data.addProperty("turn", 1);
			data.addProperty("theme", this.theme);	//주제추가
			params.add("data", data);
			for (Participant p : participants) {
				rpcNotificationService.sendNotification(p.getParticipantPrivateId(),
						ProtocolElements.PARTICIPANTSENDMESSAGE_METHOD, params);
			}

			// 게임 시간 2분 진행
			System.out.println(participants.size());
			Thread.sleep(1000*20*participants.size());
			data.addProperty("turn", 2);
			params.add("data", data);
			for (Participant p : participants) {
				rpcNotificationService.sendNotification(p.getParticipantPrivateId(),
						ProtocolElements.PARTICIPANTSENDMESSAGE_METHOD, params);
			}
			
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

}
