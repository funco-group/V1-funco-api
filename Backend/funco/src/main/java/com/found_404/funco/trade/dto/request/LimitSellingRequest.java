package com.found_404.funco.trade.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record LimitSellingRequest(
        @NotBlank
        String ticker,
        @NotNull
        @Positive
        Long price,
        @NotNull
        @Positive
        Double volume
) {
}
