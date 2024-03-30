package com.found_404.funco.follow.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;

@Builder
public record FollowingRequest(

	@NotNull
	Long memberId,
	@NotNull
	@Positive
	Long investment
) {

}
