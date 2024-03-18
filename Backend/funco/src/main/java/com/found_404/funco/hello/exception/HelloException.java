package com.found_404.funco.hello.exception;

import com.found_404.funco.global.exception.BaseException;

import lombok.Getter;

@Getter
public class HelloException extends BaseException {

	public HelloException(HelloErrorCode helloErrorCode) {
		super(helloErrorCode.getHttpStatus(), helloErrorCode.name(), helloErrorCode.getErrorMsg());
	}
}
