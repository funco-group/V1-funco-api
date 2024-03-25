package com.found_404.funco.trade.dto.response;

import lombok.Builder;

import java.util.List;

@Builder
public record HoldingCoinsResponse(
        List<String> holdingCoins
) {
}
