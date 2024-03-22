package com.found_404.funco.trade.cryptoPrice;

import com.google.gson.Gson;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class UpbitWebSocketListener extends WebSocketListener {
    private final Map<String, Long> cryptoPrices;

    public UpbitWebSocketListener() {
        this.cryptoPrices = new HashMap<>();
    }

    @Getter
    @ToString
    private static class CryptoJson {
        private String code;
        private Long trade_price;
    }

    private static CryptoJson getCryptoJson(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, CryptoJson.class);
    }

    public long getCryptoPrice(String ticker) {
        return cryptoPrices.getOrDefault(ticker, -1L);
    }

    @Override
    public void onOpen(@NotNull WebSocket webSocket, Response response) {
        log.info("upbit websocket opend [response: {}]", response.body());
    }

    /*
    *   crypto message processing
    * */
    @Override
    public void onMessage(@NotNull WebSocket webSocket, ByteString bytes) {
        String json = bytes.string(StandardCharsets.UTF_8);
        CryptoJson cryptoJson = getCryptoJson(json);
        System.out.println(cryptoJson);
        cryptoPrices.put(cryptoJson.getCode(), cryptoJson.getTrade_price());
    }


    @Override
    public void onFailure(@NotNull WebSocket webSocket, Throwable t, Response response) {
        t.printStackTrace();
    }

    @Override
    public void onClosing(WebSocket webSocket, int code, @NotNull String reason) {
        webSocket.close(1000, null);
        System.out.println("Closing : " + code + " / " + reason);
    }

    @Override
    public void onClosed(@NotNull WebSocket webSocket, int code, @NotNull String reason) {
        log.info("upbit websocket closed [code:{}, reason:{}]", code, reason);
    }

}
