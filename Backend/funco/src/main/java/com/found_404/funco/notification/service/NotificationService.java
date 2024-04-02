package com.found_404.funco.notification.service;

import com.found_404.funco.member.domain.Member;
import com.found_404.funco.member.domain.repository.MemberRepository;
import com.found_404.funco.member.exception.MemberErrorCode;
import com.found_404.funco.member.exception.MemberException;
import com.found_404.funco.notification.domain.Notification;
import com.found_404.funco.notification.domain.repository.NotificationRepository;
import com.found_404.funco.notification.domain.type.NotificationType;
import com.found_404.funco.notification.dto.NotificationDto;
import com.found_404.funco.notification.dto.SseMessage;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final SseEmitterService sseEmitterService;
    private final MemberRepository memberRepository;

    private final Map<Long, Integer> unReadCounts = new ConcurrentHashMap<>();

    public Integer getUnReadCount(Long memberId) {
        return unReadCounts.getOrDefault(memberId, 0);
    }

    public List<NotificationDto> getNotifications(Pageable pageable, Member member) {

        PageRequest pageRequest = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Direction.DESC, "id"));
        return notificationRepository.findAllByMember(pageRequest, member)
                .stream()
                .map(NotificationDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional
    public void read(Member member) {
        unReadCounts.put(member.getId(), 0);
        notificationRepository.updateRead(member.getId());
    }

    public SseEmitter subscribe(Member member) {
        return sseEmitterService.getNewSseEmitter(member.getId());
    }

    public void sendNotification(Long memberId, NotificationType notificationType, String message) {
        unReadCounts.put(memberId, unReadCounts.getOrDefault(memberId, 0) + 1);

        Notification notification = Notification.builder()
                .member(memberRepository.findById(memberId).orElseThrow(() -> new MemberException(MemberErrorCode.NOT_FOUND_MEMBER)))
                .type(notificationType)
                .message(message)
                .readYn(Boolean.FALSE)
                .build();
        notificationRepository.save(notification);

        SseMessage sseMessage = SseMessage.builder()
                .unReadCount(unReadCounts.get(memberId))
                .message(message)
                .notificationDate(LocalDateTime.now().toString())
                .build();
        sseEmitterService.sendSseMessage(memberId, sseMessage);
    }

}

