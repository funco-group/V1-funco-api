package com.found_404.funco.trade.cryptoPrice;

import com.found_404.funco.trade.exception.TradeErrorCode;
import com.found_404.funco.trade.exception.TradeException;
import com.google.gson.Gson;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;

import static com.found_404.funco.trade.exception.TradeErrorCode.PRICE_CONNECTION_FAIL;

@Component
@Slf4j
@RequiredArgsConstructor
public class UpbitCryptoPrice implements CryptoPrice {
    private static final String TICKER_URL = "https://api.upbit.com/v1/market/all";
    private static final String WEBSOCKET_URL = "wss://api.upbit.com/websocket/v1";
    private static final String PRICE_API_URL = "https://api.upbit.com/v1/ticker?markets=";
    // 설정으로 뺄 예정

    private final Set<String> markets = new HashSet<>();
    private final Gson gson;
    private final OkHttpClient httpClient;
    private final UpbitWebSocketListener listener;
    private WebSocket webSocket;

    @Autowired
    public UpbitCryptoPrice(UpbitWebSocketListener upbitWebSocketListener, OkHttpClient okHttpClient, Gson gson) {
        this.listener = upbitWebSocketListener;
        this.httpClient = okHttpClient;
        this.gson = gson;

        connectWebSocket();
    }

    private void connectWebSocket() {
        Request request = new Request.Builder().url(WEBSOCKET_URL).build();
        this.webSocket = httpClient.newWebSocket(request, listener);
    }

    private String getApiResponse(String url) {
        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = httpClient.newCall(request).execute()) {
            return Objects.isNull(response.body()) ? null : response.body().string();
        } catch (IOException e) {
            log.error("[Error] price get failed , msg: {}", e.getMessage());
            throw new TradeException(PRICE_CONNECTION_FAIL);
        }
    }

    @Getter
    @ToString
    private static class CryptoJson {
        private String market;
        private Long trade_price;
    }

    @Override
    public Map<String, Long> getTickerPriceMap(List<String> tickers) {
        CryptoJson[] cryptoJson = gson.fromJson(getApiResponse(getUrlWithParameters(tickers)), CryptoJson[].class);

        Map<String, Long> tickerPriceMap = new HashMap<>();
        Arrays.stream(cryptoJson).forEach(crypto -> tickerPriceMap.put(crypto.getMarket(), crypto.getTrade_price()));
        return tickerPriceMap;
    }

    private String getUrlWithParameters(List<String> tickers) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 1; i < tickers.size(); i++) {
            stringBuilder.append(tickers.get(i)).append(',');
        }
        stringBuilder.append(tickers.get(0));

        System.out.println(PRICE_API_URL + stringBuilder);
        return PRICE_API_URL + stringBuilder;
    }

    @Override
    public long getTickerPrice(String ticker) {
        long cryptoPrice = listener.getCryptoPrice(ticker);
        if (cryptoPrice > 0) {
            return cryptoPrice;
        }

        // 없을 시 api 요청
        try {
            CryptoJson[] cryptoJson = gson.fromJson(getApiResponse(getUrlWithParameters(List.of(ticker))), CryptoJson[].class);
            System.out.println(Arrays.toString(cryptoJson));
            return cryptoJson[0].trade_price;
        } catch (Exception e) {
            log.error("[Error] price get failed , msg: {}", e.getMessage());
        }
        throw new TradeException(PRICE_CONNECTION_FAIL);
    }

    @Override
    public void addTicker(String ticker) {
        markets.add(ticker);
        updateListenerMarkets();
    }

    @Override
    public void removeTicker(String ticker) {
        markets.remove(ticker);
        updateListenerMarkets();
    }

    private void updateListenerMarkets() {
        String message = gson.toJson(List.of(new Ticket(),
                new TypeCodes(markets.stream().toList()),
                new Format()));

        log.info("[websocket] send message: {}", message);
        webSocket.send(message);
    }



    private static class Ticket {
        private final String ticket = UUID.randomUUID().toString();
    }

    private static class TypeCodes {
        private final String type = "trade";
        private final List<String> codes; // market

        TypeCodes(List<String> codes) {
            this.codes = codes;
        }
    }


    private static class Format {
        private final String format;

        Format() {
            this.format = "DEFAULT";
        }

        Format(String format) {
            this.format = format;
        }
    }
}
