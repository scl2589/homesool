package io.openvidu.server.game;

import java.util.StringTokenizer;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

public class InitialGameUtil {

	private final RestTemplate restTemplate = new RestTemplate();
	private final String dictUrl = "https://stdict.korean.go.kr/api/search.do";
	private final String dictKey = "27E420D408CA92272CCC8E8A586CA671";

	public String Direct(char c) {
		String chosung = null;
		int first = (c - 44032) / (21 * 28);
		switch (first) {
		case 0:
		case 1:
			chosung = "ㄱ";
			break;
		case 2:
			chosung = "ㄴ";
			break;
		case 3:
		case 4:
			chosung = "ㄷ";
			break;
		case 5:
			chosung = "ㄹ";
			break;
		case 6:
			chosung = "ㅁ";
			break;
		case 7:
		case 8:
			chosung = "ㅂ";
			break;
		case 9:
		case 10:
			chosung = "ㅅ";
			break;
		case 11:
			chosung = "ㅇ";
			break;
		case 12:
		case 13:
			chosung = "ㅈ";
			break;
		case 14:
			chosung = "ㅊ";
			break;
		case 15:
			chosung = "ㅋ";
			break;
		case 16:
			chosung = "ㅌ";
			break;
		case 17:
			chosung = "ㅍ";
			break;
		case 18:
			chosung = "ㅎ";
			break;

		}
		return chosung;
	}

	public boolean searchWord(String word) {
		HttpHeaders httpHeaders = new HttpHeaders();
		UriComponents uri = UriComponentsBuilder.fromHttpUrl(dictUrl + "?key=" + dictKey + "&q=" + word).build();
		HttpEntity<?> httpEntity = new HttpEntity<>(httpHeaders);
		String ret = restTemplate.exchange(uri.toString(), HttpMethod.GET, httpEntity, String.class).getBody();
		
		StringTokenizer st = new StringTokenizer(ret, "\n");
		
		// 16번째 줄
		for (int i = 0; i < 16; i++)
			st.nextToken();
		String total = st.nextToken();
		boolean flag;
		// <total> 다음에 오는 숫자
		if (total.trim().charAt(7) == '0') {
			flag = false;
		} else {
			flag = true;
		}
		return flag;
	}
}
