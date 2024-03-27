package com.found_404.funco.favoritecoin.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.found_404.funco.favoritecoin.dto.FavoriteCoinInfo;
import com.found_404.funco.favoritecoin.dto.request.FavoriteCoinRequest;
import com.found_404.funco.favoritecoin.dto.response.FavoriteCoinResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FavoriteCoinService {
	private final RedisTemplate<Long, Object> favoriteCoinRedisTemplate;

	public void createFavoriteCoin(Long memberId, FavoriteCoinRequest favoriteCoinRequest) {
		FavoriteCoinInfo favoriteCoinInfo = readFavoriteCoinInfo(memberId);
		favoriteCoinInfo.createFavorite(favoriteCoinRequest.ticker());
		updateFavoriteInfo(memberId, favoriteCoinInfo); // redis에 바뀐 정보 업데이트
	}

	public FavoriteCoinResponse readFavoriteCoin(Long memberId) {
		FavoriteCoinInfo favoriteCoinInfo = readFavoriteCoinInfo(memberId);
		Set<String> tickerSet = favoriteCoinInfo.getTickerSet();
		List<String> tickers = new ArrayList<>(tickerSet != null ? tickerSet : new ArrayList<>());
		return FavoriteCoinResponse.builder()
			.tickers(tickers)
			.build();
	}

	public void deleteFavoriteCoin(Long memberId, FavoriteCoinRequest favoriteCoinRequest) {
		FavoriteCoinInfo favoriteCoinInfo = readFavoriteCoinInfo(memberId);
		if (favoriteCoinInfo == null) {
			return;
		}
		favoriteCoinInfo.deleteFavorite(favoriteCoinRequest.ticker());
		updateFavoriteInfo(memberId, favoriteCoinInfo); // redis에 바뀐 정보 업데이트
	}

	private FavoriteCoinInfo readFavoriteCoinInfo(Long memberId) {
		FavoriteCoinInfo favoriteCoinInfo = (FavoriteCoinInfo)favoriteCoinRedisTemplate.opsForValue().get(memberId);
		if (favoriteCoinInfo == null) {
			favoriteCoinInfo = FavoriteCoinInfo.builder().build();
		}
		return favoriteCoinInfo;
	}

	private void updateFavoriteInfo(Long memberId, FavoriteCoinInfo favoriteCoinInfo) {
		favoriteCoinRedisTemplate.opsForValue().set(memberId, favoriteCoinInfo);
	}
}
