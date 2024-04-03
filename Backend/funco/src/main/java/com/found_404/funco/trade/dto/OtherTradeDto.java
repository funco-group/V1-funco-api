package com.found_404.funco.trade.dto;

import com.found_404.funco.trade.domain.Trade;
import com.found_404.funco.trade.domain.type.TradeType;
import lombok.Builder;

import java.time.LocalDateTime;


// 급하게 임시방편 dto
@Builder
public record OtherTradeDto(
        LocalDateTime date, // 날짜
        String name, // ticker
        String assetType, // COIN
        Double volume,
        TradeType tradeType, // BUY, SELL
        Long price, // 단가
        Long orderCash, // 거래 금액
        Long commission, // null
        Long settlement // null
) {
    public static OtherTradeDto fromEntity(Trade trade) {
        return OtherTradeDto.builder()
                .date(trade.getCreatedAt())
                .name(trade.getTicker())
                .assetType("COIN")
                .volume(trade.getVolume())
                .tradeType(trade.getTradeType())
                .price(trade.getPrice())
                .orderCash(trade.getOrderCash())
                .commission(null)
                .settlement(null)
                .build();
    }
}
