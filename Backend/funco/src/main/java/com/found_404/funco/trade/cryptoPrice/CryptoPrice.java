package com.found_404.funco.trade.cryptoPrice;

import java.util.List;
import java.util.Map;

public interface CryptoPrice {
    Map<String, Long> getTickerPriceMap(List<String> tickers) throws RuntimeException;
    long getTickerPrice(String ticker) throws RuntimeException;
    void addTicker(String ticker);
    void removeTicker(String ticker);
}
