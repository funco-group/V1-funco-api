package com.found_404.funco.auth.exception;

import com.found_404.funco.global.exception.BaseException;

public class AuthException extends BaseException {
	public AuthException(AuthErrorCode authErrorCode) {
		super(authErrorCode.getHttpStatus(), authErrorCode.name(), authErrorCode.getErrorMsg());
	}
}
