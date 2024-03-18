package com.found_404.funco.global.exception;

import lombok.Getter;

@Getter
public class ErrorResponse {

	private final String errorCode;
	private final String message;

	public ErrorResponse(BaseException baseException) {
		this.errorCode = baseException.getErrorCode();
		this.message = baseException.getMessage();
	}
}
