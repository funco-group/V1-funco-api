package com.found_404.funco.trade.cryptoPrice;

import com.found_404.funco.global.util.HttpClientUtil;
import com.found_404.funco.trade.cryptoPrice.jsonObject.CryptoJson;
import com.found_404.funco.trade.domain.type.TradeType;
import com.found_404.funco.trade.exception.TradeException;
import com.found_404.funco.trade.service.OpenTradeService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static com.found_404.funco.trade.exception.TradeErrorCode.PRICE_CONNECTION_FAIL;

@Slf4j
@RequiredArgsConstructor
@Component
public class UpbitWebSocketListener extends WebSocketListener {
    private final HttpClientUtil httpClientUtil;
    private final OpenTradeService openTradeService;

    private final Map<String, Long> cryptoPrices = new HashMap<>();
    private final ConcurrentHashMap<String, PriorityQueue<ProcessingTrade>> buyTrades = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, PriorityQueue<ProcessingTrade>> sellTrades = new ConcurrentHashMap<>();


    @AllArgsConstructor
    static class ProcessingTrade {
        Long id;
        Long price;
    }

    public void addTrade(TradeType tradeType, String ticker, Long id, Long price) {
        buyTrades.putIfAbsent(ticker, new PriorityQueue<>((t1, t2) -> Long.compare(t2.price, t1.price))); // 최소힙
        sellTrades.putIfAbsent(ticker, new PriorityQueue<>((t1, t2) -> Long.compare(t1.price, t2.price))); // 최대힙

        if (tradeType.equals(TradeType.BUY)) {
            buyTrades.get(ticker).add(new ProcessingTrade(id, price));
        } else {
            sellTrades.get(ticker).add(new ProcessingTrade(id, price));
        }
    }

    public long getCryptoPrice(String ticker) {
        return cryptoPrices.getOrDefault(ticker, -1L);
    }

    @Override
    public void onOpen(@NotNull WebSocket webSocket, Response response) {
        log.info("upbit websocket opened [response: {}]", response.body());
    }

    /*
    *   crypto message processing
    * */
    @Override
    public void onMessage(@NotNull WebSocket webSocket, ByteString bytes) {
        String response = bytes.string(StandardCharsets.UTF_8);
        CryptoJson cryptoJson = httpClientUtil.parseJsonToClass(response, CryptoJson.class)
                .orElseThrow(() -> new TradeException(PRICE_CONNECTION_FAIL));

        priceUpdate(cryptoJson.getCode(), cryptoJson.getTradePrice());
    }

    private void priceUpdate(String code, Long tradePrice) {
        if (tradePrice.equals(cryptoPrices.getOrDefault(code, -1L))) {
            return; // 가격이 같으면 업데이트 x
        }
        cryptoPrices.put(code, tradePrice);

        processTrade(code, tradePrice);
    }

    public void processTrade(String code, Long tradePrice) {
        List<Long> concludingTradeIds = new ArrayList<>();

        PriorityQueue<ProcessingTrade> buyQueue = buyTrades.get(code);
        while (!buyQueue.isEmpty() && buyQueue.peek().price >= tradePrice) {
            concludingTradeIds.add(buyQueue.poll().id);
        }

        PriorityQueue<ProcessingTrade> sellQueue = sellTrades.get(code);
        while (!sellQueue.isEmpty() && sellQueue.peek().price <= tradePrice) {
            concludingTradeIds.add(sellQueue.poll().id);
        }


        // 거래 처리
        if (!concludingTradeIds.isEmpty()) {
            log.info("{} price:{} ,체결: {}개", code, tradePrice, concludingTradeIds.size());

            //int sizeSum = buyTrades.get(code).size() + sellTrades.get(code).size();
            openTradeService.processTrade(concludingTradeIds, tradePrice);
        }
    }

    @Override
    public void onFailure(@NotNull WebSocket webSocket, Throwable t, Response response) {
        log.error("upbit websocket error! msg:{}, response:{} ", t.getMessage(), response == null ? "null" : response.message());
    }

    @Override
    public void onClosing(WebSocket webSocket, int code, @NotNull String reason) {
        webSocket.close(1000, null);
    }

    @Override
    public void onClosed(@NotNull WebSocket webSocket, int code, @NotNull String reason) {
        log.info("upbit websocket closed [code:{}, reason:{}]", code, reason);
    }

}
