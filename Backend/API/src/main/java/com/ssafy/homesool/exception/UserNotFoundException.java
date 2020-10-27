package com.ssafy.homesool.exception;

import org.springframework.http.HttpStatus;

public class UserNotFoundException extends BusinessException {
	public UserNotFoundException(long userId) {
		super(String.format("userId %d is not exist.", userId), HttpStatus.BAD_REQUEST);
	}
}
