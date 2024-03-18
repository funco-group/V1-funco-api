package com.found_404.funco.hello.exception;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum HelloErrorCode {
	HELLO_NOT_FOUND(HttpStatus.NOT_FOUND, "Hello를 찾을 수 없었어요. 아쉽게도.");

	private final HttpStatus httpStatus;
	private final String errorMsg;

}
