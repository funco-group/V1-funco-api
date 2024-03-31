package com.found_404.funco.asset.dto.response;

import jakarta.validation.constraints.NotNull;

public record CryptoResponse(
        @NotNull
        Double volume
) {
}
