package com.found_404.funco.trade.domain.repository;

import com.found_404.funco.trade.domain.Trade;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TradeRepository extends JpaRepository<Trade, Long>, QueryDslTradeRepository {

}
