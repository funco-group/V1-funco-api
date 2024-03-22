package com.found_404.funco.trade;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.Getter;
import lombok.ToString;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class WebSocketClientApp {
    private static final String TICKER_URL = "https://api.upbit.com/v1/market/all";
    private static final String SOCKET_URL = "wss://api.upbit.com/websocket/v1";
    private static final OkHttpClient httpClient = new OkHttpClient();


    private static String requestAPI(String url) {
        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = httpClient.newCall(request).execute()) {
            if (Objects.nonNull(response.body())) {
                return response.body().string();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    @Getter
    private static class TickerJson {
        private String market;
    }

    private static List<String> getTickers(String json) {
        Gson gson = new Gson();
        Type listType = new TypeToken<List<TickerJson>>(){}.getType();
        List<TickerJson> tickerJsons = gson.fromJson(json, listType);

        return tickerJsons
                .stream()
                .map(TickerJson::getMarket)
                .collect(Collectors.toList());
    }

    @Getter
    @ToString
    public static class CryptoJson {
        private String code;
        private Long trade_price;
    }

    private static CryptoJson getCryptoPrices(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, CryptoJson.class);
    }

    public static void main(String[] args) {
        String json = requestAPI(TICKER_URL);
        System.out.println(getTickers(json));

        Request request = new Request.Builder().url(SOCKET_URL).build();
        WebSocketListener listener = new EchoWebSocketListener();

        WebSocket ws = httpClient.newWebSocket(request, listener);

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            ws.close(1000, "Goodbye !");
        }
    }

    private static final class EchoWebSocketListener extends WebSocketListener {
//        @Data(staticConstructor = "of")
//        private static class Ticket {
//            private final String ticket;
//        }
//
//        @Data(staticConstructor = "of")
//        private static class Type {
//            private final String type;
//            private final List<String> codes; // market
//        }
//
//        @Data(staticConstructor = "of")
//        private static class Format {
//            private final String format;
//        }

        private static class Ticket {
            private final String ticket = "qwe123";
        }

        private static class Type {
            private final String type = "trade";
            private final List<String> codes; // market

            public Type(List<String> codes) {
                this.codes = codes;
            }
        }

        private static class Format {
            private final String format = "DEFAULT";
        }

        static int count;

        @Override
        public void onClosed(@NotNull WebSocket webSocket, int code, @NotNull String reason) {
            super.onClosed(webSocket, code, reason);
            System.out.println("닫힘, 호출 횟수 : " + count);
        }

        @Override
        public void onOpen(WebSocket webSocket, Response response) {
            System.out.println("connected");
            Gson gson = new Gson();
            String type = "trade";
            List<String> codes = new ArrayList<>();
            codes.add("KRW-ETH");
            codes.add("KRW-BTC");

            //String message = gson.toJson(List.of(Ticket.of(UUID.randomUUID().toString()), Type.of(type, codes), Format.of("DEFAULT")));
            String message = gson.toJson(List.of(new Ticket(), new Type(codes), new Format()));
            System.out.println("내가생성한 메시지 " + message);

            webSocket.send(message);
        }

        @Override
        public void onMessage(WebSocket webSocket, ByteString bytes) {
            String json = bytes.string(StandardCharsets.UTF_8);
            System.out.println(getCryptoPrices(json));
            count++;
        }

        @Override
        public void onClosing(WebSocket webSocket, int code, String reason) {
            webSocket.close(1000, null);
            System.out.println("Closing : " + code + " / " + reason);
        }

        @Override
        public void onFailure(WebSocket webSocket, Throwable t, Response response) {
            t.printStackTrace();
        }
    }
}
