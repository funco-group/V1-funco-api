package com.found_404.funco.notification.domain.repository;

import com.found_404.funco.member.domain.Member;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.found_404.funco.notification.domain.Notification;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findAllByMember(Pageable pageable, Member member);

    @Modifying
    @Query("UPDATE Notification n SET n.readYn = true WHERE n.member.id = :memberId")
    void updateRead(@Param("memberId") Long memberId);

}
