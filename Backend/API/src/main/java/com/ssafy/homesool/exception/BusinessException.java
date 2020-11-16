package com.ssafy.homesool.exception;

import org.springframework.http.HttpStatus;

// Exception Handling을 위한 Custom Exception Class
public class BusinessException extends RuntimeException {
	HttpStatus code = HttpStatus.NOT_FOUND;

	public BusinessException(String message) {
		super(message);
	}

	public BusinessException(String message, HttpStatus code) {
		super(message);
		this.code = code;
	}

	public HttpStatus getCode() {
		return code;
	}
}
