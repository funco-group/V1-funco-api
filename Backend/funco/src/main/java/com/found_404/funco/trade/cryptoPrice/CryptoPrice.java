package com.found_404.funco.trade.cryptoPrice;

import com.found_404.funco.trade.domain.type.TradeType;

import java.util.List;
import java.util.Map;

public interface CryptoPrice {
    Map<String, Long> getTickerPriceMap(List<String> tickers) throws RuntimeException;
    long getTickerPrice(String ticker) throws RuntimeException;

    void addTrade(String ticker, Long id, TradeType tradeType, Long price);

    void removeTicker(String ticker);
}
