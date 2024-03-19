package com.found_404.funco.favoritecoin.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.found_404.funco.favoritecoin.domain.FavoriteCoin;

public interface FavoriteCoinRepository extends JpaRepository<FavoriteCoin, Long> {
}
