package com.found_404.funco.global.util;

import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class HttpClientUtil {
    private final OkHttpClient httpClient;
    private final Gson gson;

    public String getApiResponse(String url) {
        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = httpClient.newCall(request).execute()) {
            return Objects.isNull(response.body()) ? null : response.body().string();
        } catch (Exception e) {
            log.error("[Error] price get failed , msg: {}", e.getMessage());
            return null;
        }
    }

    public <T> Optional<T> parseJsonToClass(String jsonResponse, Class<T> classType) {
        try {
            return Optional.of(gson.fromJson(jsonResponse, classType));
        } catch (Exception e) {
            log.error("[Error] price get failed , msg: {}", e.getMessage());
            return Optional.empty();
        }
    }

    public String toJson(Object object) {
        return gson.toJson(object);
    }

    public WebSocket getWebSocket(Request request, WebSocketListener webSocketListener) {
        return httpClient.newWebSocket(request, webSocketListener);
    }

}
