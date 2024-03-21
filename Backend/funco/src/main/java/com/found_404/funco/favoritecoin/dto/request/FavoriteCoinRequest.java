package com.found_404.funco.favoritecoin.dto.request;

import lombok.Builder;

@Builder
public record FavoriteCoinRequest(
	String ticker
) {
}
