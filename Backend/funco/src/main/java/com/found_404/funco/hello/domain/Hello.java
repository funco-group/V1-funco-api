package com.found_404.funco.hello.domain;

import com.found_404.funco.global.entity.BaseEntity;

import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Hello extends BaseEntity {

	private String message;

	@Builder
	public Hello(String message) {
		this.message = message;
	}

	public void updateMessage(String message) {
		this.message = message;
	}
}
