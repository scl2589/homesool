package com.ssafy.homesool.dto;

import java.util.ArrayList;
import java.util.List;

import com.ssafy.homesool.entity.UserDrink;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class UserDto {
	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	public static class UserInfo {
		@ApiModelProperty(value = "User id", example = "1404739104")
		private long id;

		@ApiModelProperty(value = "User name", example = "차윤석")
		private String name;

		@ApiModelProperty(value = "User email", example = "c9boom7@naver.com")
		private String email;
		
		@ApiModelProperty(value = "User Drinks", example = ""
				+ "[{\"liquorName\" : \"소주\", \"liquorLimit\" : \"3\" }]")
		private List<UserDrink> drinks = new ArrayList<>();
	}

	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	public static class UserRequest {
		@ApiModelProperty(value = "User name", example = "차윤석")
		private String name;

		@ApiModelProperty(value = "User email", example = "c9boom7@naver.com")
		private String email;
		
		@ApiModelProperty(value = "User Drinks", example = ""
				+ "[{\"liquorName\" : \"소주\", \"liquorLimit\" : \"3\" }]")
		private List<UserDrink> drinks = new ArrayList<>();
	}

}
