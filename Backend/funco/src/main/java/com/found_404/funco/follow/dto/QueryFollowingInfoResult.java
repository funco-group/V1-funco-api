package com.found_404.funco.follow.dto;

import java.time.LocalDateTime;

import lombok.Builder;

@Builder
public record QueryFollowingInfoResult(
	Long followId,
	String nickname,
	Long investment,
	LocalDateTime followedAt,
	Long cash
) {
}
