package com.found_404.funco.favoritecoin.domain.repository.impl;

import static com.found_404.funco.favoritecoin.domain.QFavoriteCoin.*;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.found_404.funco.favoritecoin.domain.repository.FavoriteCoinCustomRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class FavoriteCoinCustomRepositoryImpl implements FavoriteCoinCustomRepository {
	private final JPAQueryFactory jpaQueryFactory;

	@Override
	public List<String> findFavoriteCoinTickersByMemberId(Long memberId) {
		return jpaQueryFactory
			.select(favoriteCoin.ticker)
			.from(favoriteCoin)
			.fetch();
	}
}
