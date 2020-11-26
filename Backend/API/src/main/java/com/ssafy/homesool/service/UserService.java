package com.ssafy.homesool.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;
import com.ssafy.homesool.config.security.JwtTokenProvider;
import com.ssafy.homesool.dto.KakaoUserDto;
import com.ssafy.homesool.dto.LoginDto;
import com.ssafy.homesool.dto.UserDto;
import com.ssafy.homesool.dto.UserDto.UserRecord2;
import com.ssafy.homesool.dto.UserDto.UserRecordDetail;
import com.ssafy.homesool.entity.User;
import com.ssafy.homesool.entity.UserDrink;
import com.ssafy.homesool.entity.UserRecord;
import com.ssafy.homesool.exception.UserNotFoundException;
import com.ssafy.homesool.mapper.UserMapper;
import com.ssafy.homesool.repository.MemberRepository;
import com.ssafy.homesool.repository.PhotoRepository;
import com.ssafy.homesool.repository.RoomRepository;
import com.ssafy.homesool.repository.UserDrinkRepository;
import com.ssafy.homesool.repository.UserRecordRepository;
import com.ssafy.homesool.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserService {
	private final UserRepository userRepository;
	private final UserDrinkRepository userDrinkRepository;
	private final UserRecordRepository userRecordRepository;
	private final PhotoRepository photoRepository;
	private final MemberRepository memberRepository;
	private final RoomRepository roomRepository;

	private final JwtTokenProvider jwtTokenProvider;
	private final RestTemplate restTemplate = new RestTemplate(); // could not autowired
	private final Gson gson;
	@Value("${kakao.user.url}")
	private String kakaoUserUrl;

	public LoginDto.Response login(String accessToken) {
		LoginDto.Response response = new LoginDto.Response();

		// Kakao 서버로 accessToken 보내서 user info 받아오기
		KakaoUserDto kakaoUserDto = getUserIdFromKakao(accessToken);

		// DB에 parent id가 없으면 새로 저장
		if (!userRepository.existsById(kakaoUserDto.getId())) {
			response.setNew(true);
			try {
				save(kakaoUserDto.getId(), // id
					kakaoUserDto.getKakaoAccount().getProfile().getNickname(), // name
					kakaoUserDto.getKakaoAccount().getEmail() // email
				);
			} catch (Exception e) {
				save(kakaoUserDto.getId(), // id
					kakaoUserDto.getKakaoAccount().getProfile().getNickname(), // name
					""
				);
			}
		}
		response.setToken(jwtTokenProvider.createToken(kakaoUserDto.getId()));
		return response;
	}

	private void save(long id, String name, String email) {
		User user = User.builder()
			.id(id)
			.name(name)
			.email(email)
			.build();
		userRepository.save(user);
	}

	private KakaoUserDto getUserIdFromKakao(String accessToken) {
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.set(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);
		httpHeaders.set(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=utf-8");

		HttpEntity<?> httpEntity = new HttpEntity<>(httpHeaders);
		String ret = restTemplate.exchange(kakaoUserUrl, HttpMethod.GET, httpEntity, String.class).getBody();

		return gson.fromJson(ret, KakaoUserDto.class);
	}

	public UserDto.UserInfo findById(long userId) {
		return UserMapper.INSTANCE.to(userRepository.findById(userId)
			.orElseThrow(() -> new UserNotFoundException(userId)));
	}

	public UserDto.UserInfo update(long userId, UserDto.UserRequest userReq) {
		
		userDrinkRepository.deleteAllByUserId(userId);
		
		for(UserDrink drink : userReq.getDrinks()) {
			UserDrink userDrink = UserDrink.builder()
					.userId(userId)
					.liquorName(drink.getLiquorName())
					.liquorLimit(drink.getLiquorLimit())
					.build();
			userDrinkRepository.save(userDrink);
		}
		User user = User.builder()
			.id(userId)
			.name(userReq.getName())
			.email(userReq.getEmail())
			.build();
		return UserMapper.INSTANCE.to(userRepository.save(user));
	}

	public long updateRecord(long userId, long roomId, UserDto.UserRecord record) {
		UserRecord userRecord = UserRecord.builder()
				.userId(userId)
				.roomId(roomId)
				.liquorName(record.getLiquorName())
				.liquorLimit(record.getLiquorLimit())
				.build();
		if(record.getId() != 0)
			userRecord.setId(record.getId());
		return userRecordRepository.save(userRecord).getId();
	}
	
	public List<UserDto.UserRecord> getRecordByUserIdAndRoomId(long userId, long roomId) {
		List<UserDto.UserRecord> userRecord = null;
		userRecord = UserMapper.INSTANCE.toRecordList(
				userRecordRepository.findAllByUserIdAndRoomId(userId,roomId));
		
		return userRecord;
	}
	
	
	public UserDto.UserRecordDetail getRecord(long userId, long roomId) {
		UserRecordDetail userRecordDetail = new UserRecordDetail();
		
		
		userRecordDetail.setRecords(
				UserMapper.INSTANCE.toRecord(
						userRecordRepository.findAllByUserIdAndRoomId(userId,roomId)));
		
		userRecordDetail.setSrcs(photoRepository.findSrcByroomId(roomId));
		
		userRecordDetail.setHost(memberRepository.findHostnameByroomId(roomId));
		userRecordDetail.setUsers(memberRepository.findNicknameByroomId(roomId));
		userRecordDetail.setRoomInfo(roomRepository.findOneByRoomId(roomId));
		return userRecordDetail;
	}
	
	public void withdrawal(long userId) {
		userRepository.deleteById(userId);
	}

	public List<UserDto.UserRecord3>  getStatistics10days(long userId) {
		// 날짜별 음주량 리스트
		List<UserDto.UserRecord3> res = new ArrayList<>();
		// 최근 10번의 음주 날짜 가져오기
		List<String> drinkDayList = roomRepository.get10days(userId);
		
		for(String date : drinkDayList) {
			UserDto.UserRecord3 ur3 = new UserDto.UserRecord3();
			// 날짜 저장
			ur3.setDate(date);
			// 날짜별 음주량
			ur3.setUserRecord(UserMapper.INSTANCE.toRecord(
					userRecordRepository.getStatisticsByDate(userId, date)));
			res.add(ur3);
		}
		return res;
	}
	
	public List<UserRecord2> getStatistics(long userId) {
		return UserMapper.INSTANCE.toRecord(
				userRecordRepository.getStatistics(userId));
	}
}
