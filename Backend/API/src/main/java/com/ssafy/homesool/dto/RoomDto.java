package com.ssafy.homesool.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.ssafy.homesool.entity.UserDrink;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class RoomDto {
	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	public static class RoomInfo {
		@ApiModelProperty(value = "Roomd id")
		private long roomId;

		@ApiModelProperty(value = "Host id", example = "1404739104")
		private long hostId;

		@ApiModelProperty(value = "Start time", example = "")
		private Date startTime;
		
		@ApiModelProperty(value = "End time", example = "")
		private Date endTime;
		
		@ApiModelProperty(value = "Room code", example = "A1B2C3D4E5")
		private String code;
		
		@ApiModelProperty(value = "Room Name", example = "오늘은 내가 술게임최강자")
		private String roomName;
	}
	
	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	public static class RoomResponse {
		@ApiModelProperty(value = "Room id", example = "1")
		private long roomId;
		
		@ApiModelProperty(value = "Room code", example = "A1B2C3D4E5")
		private String code;
	}
	
	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	public static class InsertRoomInfo {
		@ApiModelProperty(value = "Host id", example = "1404739104")
		private long hostId;

		@ApiModelProperty(value = "Start time", example = "")
		private Date startTime;
		
		@ApiModelProperty(value = "Host nickname", example = "지은")
		private String hostNickName;
		
		@ApiModelProperty(value = "Room Name", example = "오늘은 내가 술게임최강자")
		private String roomName;
	}
	
	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	public static class UpdateRoomInfo {
		@ApiModelProperty(value = "Room Id", example = "1")
		private long roomId;

		@ApiModelProperty(value = "End time", example = "")
		private Date endTime;
	}
	
	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	public static class UpdateMemberInfo {
		@ApiModelProperty(value = "User Nickname", example = "지은")
		private String nickName;
	}
}
