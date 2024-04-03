package com.found_404.funco.trade.dto;

import java.time.LocalDateTime;

import com.querydsl.core.annotations.QueryProjection;

import lombok.Builder;

@Builder
public record RecentTradedCoin(
	String ticker,
	LocalDateTime createdAt
) {
	@QueryProjection

	public RecentTradedCoin(String ticker, LocalDateTime createdAt) {
		this.ticker = ticker;
		this.createdAt = createdAt;
	}
}
