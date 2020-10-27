package com.ssafy.homesool.dto;

import java.util.Date;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class UserDto {
	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	public static class UserInfo {
		@ApiModelProperty(value = "User id", example = "1486633352")
		private long id;

		@ApiModelProperty(value = "User name", example = "김싸피")
		private String name;

		@ApiModelProperty(value = "User email", example = "ssafy@ssafy.com")
		private String email;
		
		@ApiModelProperty(value = "User birth", example = "2000-01-01")
		private Date birth;
		
	}

	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	public static class UserRequest {
		@ApiModelProperty(value = "User name", example = "김싸피")
		private String name;

		@ApiModelProperty(value = "User email", example = "ssafy@ssafy.com")
		private String email;
	}
}
