package com.found_404.funco.favoritecoin.domain.repository;

import java.util.List;

public interface FavoriteCoinCustomRepository {

	List<String> findFavoriteCoinTickersByMemberId(Long memberId);
}
