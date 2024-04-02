package com.found_404.funco.notification.dto;

import lombok.Builder;

@Builder
public record SseMessage(
        int unReadCount,
        String message,
        String notificationDate
) {
}
