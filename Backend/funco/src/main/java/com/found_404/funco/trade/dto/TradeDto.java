package com.found_404.funco.trade.dto;

import com.found_404.funco.member.dto.MemberDto;
import com.found_404.funco.trade.domain.type.TradeType;
import lombok.Builder;

@Builder
public record TradeDto(
        Long id,
        MemberDto memberDto,
        String ticker,
        TradeType tradeType,
        Double volume,
        Long orderCash,
        Long price,
        Boolean concluded,
        Boolean status
) {
}
