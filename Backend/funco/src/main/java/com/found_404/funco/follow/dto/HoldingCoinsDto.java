package com.found_404.funco.follow.dto;

import lombok.Builder;

@Builder
public record HoldingCoinsDto(
	String ticker,
	Double volume
) {
}
