package com.found_404.funco.trade.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.found_404.funco.trade.domain.HoldingCoin;

public interface HoldingCoinRepository extends JpaRepository<HoldingCoin, Long> {
	
}
