package com.found_404.funco.follow.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Builder;

@Builder
public record FollowingInfo(
	Long followId,
	String nickname,
	Long investment,
	LocalDateTime followedAt,
	Long cash,
	List<CoinInfo> coins
) {

}