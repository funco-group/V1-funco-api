package com.found_404.funco.trade.domain.repository;

import com.found_404.funco.trade.domain.Trade;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface QueryDslTradeRepository {

    // 멤버, 팔로우 여부, 체결 true, 티커 or all, update 역순 정렬, 페이징
    List<Trade> findMyTradeHistoryByTicker(Long memberId, Boolean status, String ticker, Pageable pageable);
}
