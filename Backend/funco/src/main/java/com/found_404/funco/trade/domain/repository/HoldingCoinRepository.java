package com.found_404.funco.trade.domain.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.found_404.funco.member.domain.Member;
import com.found_404.funco.trade.domain.HoldingCoin;
import com.found_404.funco.trade.dto.Ticker;

public interface HoldingCoinRepository extends JpaRepository<HoldingCoin, Long> {
	Optional<HoldingCoin> findByMemberAndTicker(Member member, String ticker);

	List<Ticker> findByMember(Member member);

	List<HoldingCoin> findHoldingCoinByMember(Member member);

	Optional<HoldingCoin> findByMemberIdAndTicker(Long member_id, String ticker);
}
