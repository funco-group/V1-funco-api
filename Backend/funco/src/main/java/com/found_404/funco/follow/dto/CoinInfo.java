package com.found_404.funco.follow.dto;

import lombok.Builder;

@Builder
public record CoinInfo(
	String ticker,
	Double volume
) {
}
