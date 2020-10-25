package com.ssafy.homesool.dto;

import com.google.gson.annotations.SerializedName;

import lombok.Data;

@Data
public class KakaoUserDto {
	@SerializedName("id")
	private long id;

	@SerializedName("kakao_account")
	private KakaoAccount kakaoAccount;

	@Data
	public static class KakaoAccount {
		@SerializedName("profile")
		private Profile profile;
		@SerializedName("email")
		private String email;
	}

	@Data
	public static class Profile {
		@SerializedName("nickname")
		private String nickname;
	}
}
