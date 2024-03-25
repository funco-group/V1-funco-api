package com.found_404.funco.trade.dto;


import lombok.Builder;

@Builder
public record HoldingCoinDto(
        String ticker,
        Double volume,
        Long averagePrice
) {
}
