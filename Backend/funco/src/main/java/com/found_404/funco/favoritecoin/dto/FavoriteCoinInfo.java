package com.found_404.funco.favoritecoin.dto;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
public class FavoriteCoinInfo {
	private Set<String> tickerSet;
	private LocalDateTime updatedAt;

	public void createFavorite(String ticker) {
		if (tickerSet == null) {
			tickerSet = new HashSet<>();
		}
		tickerSet.add(ticker);
		updatedAt = LocalDateTime.now();
	}

	public void deleteFavorite(String ticker) {
		if (tickerSet != null) {
			tickerSet.remove(ticker);
			updatedAt = LocalDateTime.now();
		}
	}
}
