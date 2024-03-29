package com.found_404.funco.notification.domain.repository;

import com.found_404.funco.member.domain.Member;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.found_404.funco.notification.domain.Notification;

import java.util.Collection;
import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findAllByMember(Pageable pageable, Member member);

    List<Notification> findByIdIn(Collection<Long> id);
}
