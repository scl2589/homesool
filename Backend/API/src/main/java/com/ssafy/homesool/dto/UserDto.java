package com.ssafy.homesool.dto;

import java.util.ArrayList;
import java.util.List;

import com.ssafy.homesool.entity.Room;
import com.ssafy.homesool.entity.UserDrink;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
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
	
	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	public static class UserRecord {
		@ApiModelProperty(value = "Record id", example = "0")
		private long Id;
		
		@ApiModelProperty(value = "Liquor name", example = "소주")
		private String liquorName;
		
		@ApiModelProperty(value = "Liqour limit", example = "3")
		private int liquorLimit;
	}
	
	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	public static class UserRecordDetail {
		@ApiModelProperty(value = "User Drink Records", example = ""
				+ "[{\"liquorName\" : \"소주\", \"liquorLimit\" : \"3\" }]")
		private List<UserRecord2> records = new ArrayList<>();
		
		@ApiModelProperty(value = "Photo Sources", example = ""
				+ "[\"http:/k3a503.p.ssafy.io/images/fileName\"]")
		private List<String> srcs = new ArrayList<>();
		
		@ApiModelProperty(value = "Room Host Records", example = "")
		private String host;
		
		@ApiModelProperty(value = "Room User Records", example = "")
		private List<String> users = new ArrayList<>();
		
		@ApiModelProperty(value = "Room Info", example = "")
		private Room roomInfo;
	}
	
	@Builder
	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	public static class UserRecord2 {
		@ApiModelProperty(value = "Liquor name", example = "소주")
		private String liquorName;
		
		@ApiModelProperty(value = "Liqour limit", example = "3")
		private int liquorLimit;
	}
	@Builder
	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	public static class UserRecord3 {
		@ApiModelProperty(value = "Date", example = "2020-11-14")
		private String date;
		
		@ApiModelProperty(value = "User Drink Records", example = ""
				+ "[{\"liquorName\" : \"소주\", \"liquorLimit\" : \"3\" }]")
		private List<UserRecord2> userRecord;
	}
}
