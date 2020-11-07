package io.openvidu.server.game;

import java.lang.reflect.Method;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;

public class AsyncExceptionHandler implements AsyncUncaughtExceptionHandler {
	
	@Override
	public void handleUncaughtException(Throwable throwable, Method method, Object... obj) {
		System.out.println("Thread Error Exception");
		System.out.println("exception Message :: " + throwable.getMessage());
		System.out.println("method name :: " + method.getName());
		for(Object param : obj) {
			System.out.println("param Val ::: " + param);
		}
	}
		
}
