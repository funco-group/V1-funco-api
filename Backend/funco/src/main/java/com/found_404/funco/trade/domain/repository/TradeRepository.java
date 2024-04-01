package com.found_404.funco.trade.domain.repository;

import com.found_404.funco.member.domain.Member;
import com.found_404.funco.trade.domain.Trade;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TradeRepository extends JpaRepository<Trade, Long>, QueryDslTradeRepository {

    List<Trade> findAllByMember(Member member);
}
