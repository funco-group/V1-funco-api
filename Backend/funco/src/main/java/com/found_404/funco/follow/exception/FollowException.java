package com.found_404.funco.follow.exception;

import com.found_404.funco.global.exception.BaseException;

public class FollowException extends BaseException {
	public FollowException(FollowErrorCode followErrorCode) {
		super(followErrorCode.getHttpStatus(), followErrorCode.name(), followErrorCode.getErrorMsg());
	}
}
