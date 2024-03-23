package com.found_404.funco.notification.domain;

import org.hibernate.annotations.Comment;

import com.found_404.funco.global.entity.BaseEntity;
import com.found_404.funco.member.domain.Member;
import com.found_404.funco.notification.domain.type.NotificationType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Notification extends BaseEntity {

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id", nullable = false)
	private Member member;

	@Comment("알림타입")
	@Column(nullable = false)
	@Enumerated(value = EnumType.STRING)
	private NotificationType type;

	@Comment("알림메시지")
	@Column(nullable = false)
	private String message;

	@Comment("읽음여부")
	@Column(nullable = false)
	private Boolean readYn;

	@Builder
	public Notification(Member member, NotificationType type, String message, Boolean readYn) {
		this.member = member;
		this.type = type;
		this.message = message;
		this.readYn = readYn;
	}
}
