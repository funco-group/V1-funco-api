package com.found_404.funco.trade.dto.request;

import jakarta.validation.constraints.NotNull;

public record TradeRequest (
        String ticker,

        @NotNull
        Boolean follow
) {
}
