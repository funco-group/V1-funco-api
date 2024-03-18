package com.found_404.funco.global.type;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ErrorCode {
	// === GLOBAL BASE ERROR CODE ===
	BAD_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청 형식입니다."),
	UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "인증 정보가 누락되었습니다. 헤더에 Authorization을 포함해주세요."),
	FORBIDDEN(HttpStatus.FORBIDDEN,
		"이 리소스에 대한 접근 권한이 없습니다."),

	// ===========================================================================
	// 4xx: Client Errors
	NOT_FOUND(HttpStatus.NOT_FOUND, "Not Found"),
	METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "Method Not Allowed"),
	CONFLICT(HttpStatus.CONFLICT, "Conflict"),

	// 5xx: Server Errors
	INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error"),
	NOT_IMPLEMENTED(HttpStatus.NOT_IMPLEMENTED, "Not Implemented"),
	BAD_GATEWAY(HttpStatus.BAD_GATEWAY, "Bad Gateway"),
	SERVICE_UNAVAILABLE(HttpStatus.SERVICE_UNAVAILABLE, "Service Unavailable"),
	GATEWAY_TIMEOUT(HttpStatus.GATEWAY_TIMEOUT, "Gateway Timeout"),
	HTTP_VERSION_NOT_SUPPORTED(HttpStatus.HTTP_VERSION_NOT_SUPPORTED, "HTTP Version Not Supported"),

	// validation Errors
	METHOD_ARGUMENT_NOT_VALID(HttpStatus.BAD_REQUEST, "유효성 검사 실패");

	private final HttpStatus httpStatus;
	private final String errorMsg;
}