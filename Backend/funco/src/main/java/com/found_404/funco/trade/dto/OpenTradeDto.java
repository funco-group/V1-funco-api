package com.found_404.funco.trade.dto;

import com.found_404.funco.trade.domain.OpenTrade;
import com.found_404.funco.trade.domain.Trade;
import com.found_404.funco.trade.domain.type.TradeType;
import lombok.AccessLevel;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder(access = AccessLevel.PRIVATE)
public record OpenTradeDto(
        Long id,
        String ticker,
        TradeType tradeType,
        Double volume,
        Long orderCash,
        Long price,
        LocalDateTime tradeDate
) {

    public static OpenTradeDto fromEntity(OpenTrade openTrade) {
        return OpenTradeDto.builder()
                .id(openTrade.getId())
                .ticker(openTrade.getTicker())
                .price(openTrade.getPrice())
                .volume(openTrade.getVolume())
                .orderCash(openTrade.getOrderCash())
                .tradeType(openTrade.getTradeType())
                .tradeDate(openTrade.getCreatedAt())
                .build();
    }
}
