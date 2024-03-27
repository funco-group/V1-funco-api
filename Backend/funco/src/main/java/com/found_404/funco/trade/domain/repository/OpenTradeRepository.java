package com.found_404.funco.trade.domain.repository;

import com.found_404.funco.trade.domain.OpenTrade;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OpenTradeRepository extends JpaRepository<OpenTrade, Long>, QueryDslOpenTradeRepository {
}
