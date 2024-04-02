package com.found_404.funco.trade.domain.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TradeType {
    BUY("매수"),
    SELL("매도")
    ;

    private final String korean;
}
