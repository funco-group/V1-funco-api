package com.found_404.funco.global.exception;

import org.springframework.http.HttpStatus;

import lombok.Builder;
import lombok.Getter;

@Getter
public class BaseException extends RuntimeException {

	private final HttpStatus httpStatus;
	private final String errorCode;
	private final String message;

	@Builder
	public BaseException(HttpStatus httpStatus, String errorCode, String message) {
		this.httpStatus = httpStatus;
		this.errorCode = errorCode;
		this.message = message;
	}
}
