package com.found_404.funco.auth.dto.response;

import lombok.Builder;

@Builder
public record LoginResponse(
	Long memberId,
	String nickname,
	String profileUrl,
	String accessToken,
	Integer unReadCount
) {
}
