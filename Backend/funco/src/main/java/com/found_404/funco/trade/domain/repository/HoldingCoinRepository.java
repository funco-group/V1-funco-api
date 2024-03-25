package com.found_404.funco.trade.domain.repository;

import com.found_404.funco.member.domain.Member;
import com.found_404.funco.trade.dto.Ticker;
import org.springframework.data.jpa.repository.JpaRepository;

import com.found_404.funco.trade.domain.HoldingCoin;

import java.util.List;
import java.util.Optional;

public interface HoldingCoinRepository extends JpaRepository<HoldingCoin, Long> {
    Optional<HoldingCoin> findByMemberAndTicker(Member member, String ticker);

    List<Ticker> findByMember(Member member);
}
