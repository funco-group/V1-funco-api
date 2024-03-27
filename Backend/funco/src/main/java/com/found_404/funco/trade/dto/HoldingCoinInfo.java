package com.found_404.funco.trade.dto;

import com.querydsl.core.annotations.QueryProjection;

import lombok.Builder;

@Builder
public record HoldingCoinInfo(
	Long memberId,
	String ticker,
	Double volume
) {
	@QueryProjection
	public HoldingCoinInfo(Long memberId, String ticker, Double volume) {
		this.memberId = memberId;
		this.ticker = ticker;
		this.volume = volume;
	}
}
