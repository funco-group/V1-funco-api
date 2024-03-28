package com.found_404.funco.trade.cryptoPrice.jsonObject;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class CryptoJson {
    private String code;
    private String market;
    private Double trade_price;

    public long getTradePrice() {
        return trade_price.longValue();
    }
}