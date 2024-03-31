package com.found_404.funco.notification.controller;

import com.found_404.funco.notification.dto.NotificationDto;
import com.found_404.funco.notification.dto.request.ReadRequest;
import com.found_404.funco.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/v1/notifications")
@RequiredArgsConstructor
@RestController
public class NotificationController {
    private final NotificationService notificationService;

    @GetMapping()
    public ResponseEntity<List<NotificationDto>> getNotifications(Pageable pageable) {
        final long memberId = 1;

        return ResponseEntity.ok(notificationService.getNotifications(pageable, memberId));
    }

    @PatchMapping("/read")
    public ResponseEntity<?> readNotification(@RequestBody ReadRequest readRequest) {
        final long memberId = 1;

        notificationService.read(readRequest.readIds());
        return ResponseEntity.ok().build();
    }


}
