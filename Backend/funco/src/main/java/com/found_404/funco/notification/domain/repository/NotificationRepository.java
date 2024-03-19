package com.found_404.funco.notification.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.found_404.funco.notification.domain.Notification;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
	
}
