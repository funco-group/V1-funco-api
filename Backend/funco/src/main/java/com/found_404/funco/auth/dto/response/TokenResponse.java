package com.found_404.funco.auth.dto.response;

import lombok.Builder;

@Builder
public record TokenResponse(
	String accessToken
) {
}
