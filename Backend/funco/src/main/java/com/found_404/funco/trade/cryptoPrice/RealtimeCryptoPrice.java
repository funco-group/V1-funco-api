package com.found_404.funco.trade.cryptoPrice;

import com.google.gson.Gson;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;

@Component
@RequiredArgsConstructor
public class RealtimeCryptoPrice {
    private static final String TICKER_URL = "https://api.upbit.com/v1/market/all";
    private static final String SOCKET_URL = "wss://api.upbit.com/websocket/v1";
    private static final String PRICE_API_URL = "https://api.upbit.com/v1/ticker?markets=";

    private final Set<String> markets;
    private final Gson gson = new Gson();
    private final OkHttpClient httpClient = new OkHttpClient();
    private final UpbitWebSocketListener listener;
    private final WebSocket webSocket;

    @Autowired
    public RealtimeCryptoPrice(UpbitWebSocketListener upbitWebSocketListener) {
        this.listener = upbitWebSocketListener;

        // logic
        markets = new HashSet<>();

        Request request = new Request.Builder().url(SOCKET_URL).build();
        webSocket = httpClient.newWebSocket(request, listener);
    }

    public String getApiResponse(String url) {
        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = httpClient.newCall(request).execute()) {
            return Objects.isNull(response.body()) ? null : response.body().string();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Getter
    @ToString
    private static class CryptoJson {
        private Long trade_price;
    }

    public Long getTickerPrice(String ticker) {
        long cryptoPrice = listener.getCryptoPrice(ticker);
        if (cryptoPrice > 0) {
            return cryptoPrice;
        }

        // 없을 시 api 요청
        String apiResponse = getApiResponse(PRICE_API_URL + ticker);
        System.out.println(apiResponse);
        CryptoJson[] cryptoJson = gson.fromJson(apiResponse, CryptoJson[].class);
        return cryptoJson[0].trade_price;
    }

    public void addTicker(String ticker) {
        markets.add(ticker);
        updateListenerMarkets();
    }

    public void removeTicker(String ticker) {
        markets.remove(ticker);
        updateListenerMarkets();
    }

    private void updateListenerMarkets() {
        String message = gson.toJson(List.of(new Ticket(),
                new TypeCodes(markets.stream().toList()),
                new Format()));

        System.out.println("내가 생성한 메시지 : " + message);
        webSocket.send(message);
    }



    private static class Ticket {
        private final String ticket = UUID.randomUUID().toString();
    }

    private static class TypeCodes {
        private final String type = "trade";
        private final List<String> codes; // market

        public TypeCodes(List<String> codes) {
            this.codes = codes;
        }
    }


    private static class Format {
        private final String format;

        public Format() {
            this.format = "DEFAULT";
        }

        public Format(String format) {
            this.format = format;
        }
    }
}
