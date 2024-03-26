package com.found_404.funco.follow.dto;

import java.util.List;

import com.querydsl.core.annotations.QueryProjection;

import lombok.Builder;

@Builder
public record FollowingAssetInfo(
	Long cash,
	List<CoinInfo> coinInfos
) {
	@QueryProjection
	public FollowingAssetInfo(Long cash, List<CoinInfo> coinInfos) {
		this.cash = cash;
		this.coinInfos = coinInfos;
	}
}
