package com.ssafy.homesool.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class PhotoDto {
	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	public static class PhotoRequest {
		@ApiModelProperty(value = "room_id")
		private String roomId;
	}

}
