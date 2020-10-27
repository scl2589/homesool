package com.ssafy.homesool.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;

import lombok.RequiredArgsConstructor;

import com.ssafy.homesool.config.security.JwtTokenProvider;
import com.ssafy.homesool.dto.KakaoUserDto;
import com.ssafy.homesool.dto.LoginDto;
import com.ssafy.homesool.dto.UserDto;
import com.ssafy.homesool.entity.User;
import com.ssafy.homesool.entity.UserDrink;
import com.ssafy.homesool.exception.UserNotFoundException;
import com.ssafy.homesool.mapper.UserMapper;
import com.ssafy.homesool.repository.UserDrinkRepository;
import com.ssafy.homesool.repository.UserRepository;

@RequiredArgsConstructor
@Service
public class UserService {
	private final UserRepository userRepository;
	private final UserDrinkRepository userDrinkRepository;

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
			save(kakaoUserDto.getId(), // id
				kakaoUserDto.getKakaoAccount().getProfile().getNickname(), // name
				kakaoUserDto.getKakaoAccount().getEmail() // email
			);
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
		
		//기존 주량 정보 무조건 삭제
		userDrinkRepository.deleteAllByUserId(userId);
		
		//saveAll 사용하면 한번에 넣을 수 있을텐데 잘 모르겠음
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
//			.drinks(userReq.getDrinks())
			.build();
		//바로 리턴하면 drinks 값이 안넘어감..
		return UserMapper.INSTANCE.to(userRepository.save(user));
		//findById 이거해도 drinks 값이 안넘어감...왜지??
//		return UserMapper.INSTANCE.to(userRepository.findById(userId)
//				.orElseThrow(() -> new UserNotFoundException(userId)));
	}

	public void withdrawal(long userId) {
		userRepository.deleteById(userId);
	}

}
