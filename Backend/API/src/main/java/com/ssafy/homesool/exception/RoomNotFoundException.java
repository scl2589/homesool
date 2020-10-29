package com.ssafy.homesool.exception;

import org.springframework.http.HttpStatus;

public class RoomNotFoundException extends BusinessException {
	public RoomNotFoundException(String code) {
		super(String.format("Room code %s is not exist.", code), HttpStatus.NOT_FOUND);
	}
}
