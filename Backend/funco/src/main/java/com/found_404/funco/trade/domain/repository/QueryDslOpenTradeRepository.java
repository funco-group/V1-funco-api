package com.found_404.funco.trade.domain.repository;

import com.found_404.funco.trade.domain.OpenTrade;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface QueryDslOpenTradeRepository {

    List<OpenTrade> findMyOpenTrade(Long memberId, String ticker, Pageable pageable);
}
