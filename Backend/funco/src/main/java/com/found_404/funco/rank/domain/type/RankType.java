package com.found_404.funco.rank.domain.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RankType {
	ASSET("ASSET"),
	FOLLOWER_CASH("FOLLOWER_CASH");

	private final String description;
}
