package com.found_404.funco.notification.service;

import com.found_404.funco.global.util.HttpClientUtil;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@RequiredArgsConstructor
@Service
public class SseEmitterService {
    private final HttpClientUtil httpClientUtil;
    private final Map<Long, SseEmitter> sseEmitters = new ConcurrentHashMap<>();


    public SseEmitter getNewSseEmitter(Long memberId) {
        SseEmitter sseEmitter = new SseEmitter(30 * 1000L); // 만료시간 톰캣 디폴트 30초
        sseEmitters.put(memberId, sseEmitter); // 에미터 저장소에 추가

        sseEmitter.onCompletion(() -> {
            sseEmitters.remove(memberId);    // 만료되면 리스트에서 삭제
        });
        sseEmitter.onTimeout(sseEmitter::complete);

        // 더미데이터 전송
        sendEmitter(sseEmitter, memberId, "open", "connect");
        return sseEmitter;
    }

    public void sendSseMessage(Long memberId, Object data) {
        if (sseEmitters.containsKey(memberId)) {
            sendEmitter(sseEmitters.get(memberId), memberId, "message", httpClientUtil.toJson(data));
        }
    }

    private void sendEmitter(SseEmitter sseEmitter, Long memberId, String name, String data) {
        try {
            sseEmitter.send(SseEmitter.event()
                    .name(name) // 이벤트 이름
                    .data(new Gson().toJson(data))); // 데이터
            log.info("member :{} 에게 event sent! : {}", memberId, data);
        } catch (IOException e) {
            sseEmitters.remove(memberId);
            log.error("SseEmitter error memberId = {}", memberId);
        }
    }

}
