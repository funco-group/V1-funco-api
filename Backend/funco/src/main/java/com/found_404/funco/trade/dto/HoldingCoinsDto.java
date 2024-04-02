package com.found_404.funco.trade.dto;

import com.querydsl.core.annotations.QueryProjection;

import lombok.Builder;

@Builder
public record HoldingCoinsDto(
	String ticker,
	Double volume
) {
	@QueryProjection
	public HoldingCoinsDto(String ticker, Double volume) {
		this.ticker = ticker;
		this.volume = volume;
	}
}
