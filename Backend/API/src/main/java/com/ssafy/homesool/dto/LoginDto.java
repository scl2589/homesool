package com.ssafy.homesool.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class LoginDto {
	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	public static class AccessToken {
		@ApiModelProperty(value = "카카오 로그인 후 받는 access token")
		private String accessToken;
	}

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	public static class Response {
		@ApiModelProperty(value = "api 요청에 사용하는 JWT 토큰")
		private String token;

		@ApiModelProperty(value = "신규 가입 여부. true: 신규, false: 기존")
		private boolean isNew = false;
	}
}

