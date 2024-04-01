package com.found_404.funco.asset.dto.response;

import com.found_404.funco.asset.dto.type.AssetType;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record HistoryResponse(

        LocalDateTime date,
        String name,
        AssetType assetType,
        String tradeType,
        Double volume,
        Long orderCash,
        Long price,
        Long commission,
        Long settlement

) {
}
