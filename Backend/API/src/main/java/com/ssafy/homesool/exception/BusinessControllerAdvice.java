package com.ssafy.homesool.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class BusinessControllerAdvice {

	@ExceptionHandler(BusinessException.class)
	public ResponseEntity<?> businessLogicHandle(BusinessException exception) {
		return new ResponseEntity<>(exception.getMessage(), exception.getCode());
	}
}
