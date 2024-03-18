package com.found_404.funco.global.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@MappedSuperclass
public class BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@CreatedDate
	@Column(columnDefinition = "datetime(0) default now(0)", nullable = false, updatable = false)
	private LocalDateTime createdAt;

	@LastModifiedDate
	@Column(columnDefinition = "datetime(0) default now(0)", nullable = false)
	private LocalDateTime updatedAt;
}
