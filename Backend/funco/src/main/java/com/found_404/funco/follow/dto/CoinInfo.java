package com.found_404.funco.follow.dto;

import com.querydsl.core.annotations.QueryProjection;

import lombok.Builder;

@Builder
public record CoinInfo(
	String ticker,
	Double volume
) {
	@QueryProjection
	public CoinInfo(String ticker, Double volume) {
		this.ticker = ticker;
		this.volume = volume;
	}
}
