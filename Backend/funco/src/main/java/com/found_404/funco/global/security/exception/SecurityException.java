package com.found_404.funco.global.security.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public class SecurityException extends RuntimeException {
	private final SecurityErrorCode errorCode;
	private final String errorMessage;
	private final HttpStatus httpStatus;

	public SecurityException(SecurityErrorCode errorCode, HttpStatus httpStatus) {
		this.errorCode = errorCode;
		this.errorMessage = errorCode.getMessage();
		this.httpStatus = httpStatus;
	}
}
