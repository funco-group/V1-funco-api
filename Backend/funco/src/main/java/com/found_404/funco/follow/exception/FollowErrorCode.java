package com.found_404.funco.follow.exception;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FollowErrorCode {
	FOLLOW_NOT_FOUND(HttpStatus.BAD_REQUEST, "해당 팔로우를 찾을 수 없습니다."),
	FOLLOW_DUPLICATED_ERROR(HttpStatus.BAD_REQUEST, "중복된 팔로우입니다.");

	private final HttpStatus httpStatus;
	private final String errorMsg;
}
