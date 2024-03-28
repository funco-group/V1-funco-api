package com.found_404.funco.trade.cryptoPrice;

import com.found_404.funco.global.util.HttpClientUtil;
import com.found_404.funco.trade.cryptoPrice.jsonObject.CryptoJson;
import com.found_404.funco.trade.cryptoPrice.jsonObject.Format;
import com.found_404.funco.trade.cryptoPrice.jsonObject.Ticket;
import com.found_404.funco.trade.cryptoPrice.jsonObject.TypeCodes;
import com.found_404.funco.trade.domain.type.TradeType;
import com.found_404.funco.trade.exception.TradeException;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

import static com.found_404.funco.trade.exception.TradeErrorCode.PRICE_CONNECTION_FAIL;

@Component
@Slf4j
public class UpbitCryptoPrice implements CryptoPrice {
    private static final String WEBSOCKET_URL = "wss://api.upbit.com/websocket/v1";
    private static final String PRICE_API_URL = "https://api.upbit.com/v1/ticker?markets=";
    // 설정으로 뺄 예정

    private final Set<String> markets = new HashSet<>();
    private final UpbitWebSocketListener listener;
    private final HttpClientUtil httpClientUtil;
    private WebSocket webSocket;

    @Autowired
    public UpbitCryptoPrice(UpbitWebSocketListener listener, HttpClientUtil httpClientUtil) {
        this.listener = listener;
        this.httpClientUtil = httpClientUtil;

        connectWebSocket();
    }

    private void connectWebSocket() {
        Request request = new Request.Builder().url(WEBSOCKET_URL).build();
        this.webSocket = httpClientUtil.getWebSocket(request, listener);
    }

    @Override
    public Map<String, Long> getTickerPriceMap(List<String> tickers) {
        if (tickers.isEmpty()) {
            return Collections.emptyMap();
        }

        String apiResponse = httpClientUtil.getApiResponse(getUrlWithParameters(tickers));
        if (Objects.isNull(apiResponse)) {
            throw new TradeException(PRICE_CONNECTION_FAIL);
        }
        CryptoJson[] cryptoJsons = httpClientUtil.parseJsonToClass(apiResponse, CryptoJson[].class)
                .orElseThrow(() -> new TradeException(PRICE_CONNECTION_FAIL));

        Map<String, Long> tickerPriceMap = new HashMap<>();
        Arrays.stream(cryptoJsons).forEach(crypto -> tickerPriceMap.put(crypto.getMarket(), crypto.getTradePrice()));
        return tickerPriceMap;
    }

    @Override
    public long getTickerPrice(String ticker) {
        long cryptoPrice = listener.getCryptoPrice(ticker);
        if (cryptoPrice > 0) {
            return cryptoPrice;
        }

        // 없을 시 api 요청
        String apiResponse = httpClientUtil.getApiResponse(getUrlWithParameters(List.of(ticker)));
        if (Objects.isNull(apiResponse)) {
            throw new TradeException(PRICE_CONNECTION_FAIL);
        }

        CryptoJson[] cryptoJsons = httpClientUtil.parseJsonToClass(apiResponse, CryptoJson[].class)
                .orElseThrow(() -> new TradeException(PRICE_CONNECTION_FAIL));
        return cryptoJsons[0].getTradePrice();
    }

    private void addTicker(String ticker) {
        if (!markets.contains(ticker)) {
            markets.add(ticker);
            updateListenerMarkets();
        }
    }

    @Override
    public void removeTicker(String ticker) {
        if (markets.contains(ticker)) {
            markets.remove(ticker);
            updateListenerMarkets();
        }
    }

    // 감지될 예약 거래 등록
    @Override
    public void addTrade(String ticker, Long id, TradeType tradeType, Long price) {
        addTicker(ticker);

        listener.addTrade(tradeType, ticker, id, price);
    }

    private String getUrlWithParameters(List<String> tickers) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 1; i < tickers.size(); i++) {
            stringBuilder.append(tickers.get(i)).append(',');
        }
        stringBuilder.append(tickers.get(0));

        return PRICE_API_URL + stringBuilder;
    }

    private void updateListenerMarkets() {
        String message = httpClientUtil.toJson(List.of(new Ticket(),
                new TypeCodes(markets.stream().toList()),
                new Format()));

        log.info("[websocket] send message: {}", message);
        webSocket.send(message);
    }

}
