package com.found_404.funco.notification.dto;

import com.found_404.funco.notification.domain.Notification;
import com.found_404.funco.notification.domain.type.NotificationType;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record NotificationDto(
        Long id,
        NotificationType notificationType,
        String message,
        Boolean readYn,
        LocalDateTime notificationDate
) {
    public static NotificationDto fromEntity(Notification notification) {
        return NotificationDto.builder()
                .id(notification.getId())
                .notificationType(notification.getType())
                .message(notification.getMessage())
                .readYn(notification.getReadYn())
                .notificationDate(notification.getCreatedAt())
                .build();
    }

}
