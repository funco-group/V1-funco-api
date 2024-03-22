package com.found_404.funco.trade.dto.response;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;

@Builder
public record MarketTradeResponse(
        @NotBlank
        String ticker,
        @NotNull
        @Positive
        Double volume,
        @NotNull
        @Positive
        Long price
) {
}
