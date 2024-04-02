package com.found_404.funco.follow.dto;

import com.found_404.funco.follow.domain.FollowTrade;
import com.found_404.funco.trade.domain.type.TradeType;
import lombok.AccessLevel;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder(access = AccessLevel.PRIVATE)
public record FollowTradeDto(
        Long id,
        String ticker,
        TradeType tradeType,
        Double volume,
        Long orderCash,
        Long price,
        LocalDateTime tradeDate
) {

    public static FollowTradeDto fromEntity(FollowTrade followTrade) {
        return FollowTradeDto.builder()
                .id(followTrade.getId())
                .ticker(followTrade.getTicker())
                .price(followTrade.getPrice())
                .volume(followTrade.getVolume())
                .orderCash(followTrade.getOrderCash())
                .tradeType(followTrade.getTradeType())
                .tradeDate(followTrade.getCreatedAt())
                .build();
    }
}
