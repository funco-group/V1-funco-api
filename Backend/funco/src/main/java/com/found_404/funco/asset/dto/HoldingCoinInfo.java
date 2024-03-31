package com.found_404.funco.asset.dto;

import lombok.Builder;

@Builder
public record HoldingCoinInfo(
        String ticker,
        Double volume,
        Long averagePrice
) {
}
