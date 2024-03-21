package com.found_404.funco.favoritecoin.dto.response;

import java.util.List;

import lombok.Builder;

@Builder
public record FavoriteCoinResponse(
	List<String> tickers
) {
}
