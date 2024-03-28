package com.found_404.funco.follow.domain.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SettleType {
	ALL("all"),
	FOLLOWING("following"),
	SETTLED("settled");

	private final String value;
}
