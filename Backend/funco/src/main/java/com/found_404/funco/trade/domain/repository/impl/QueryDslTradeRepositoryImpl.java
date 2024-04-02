package com.found_404.funco.trade.domain.repository.impl;

import com.found_404.funco.trade.domain.Trade;
import com.found_404.funco.trade.domain.repository.QueryDslTradeRepository;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;

import static com.found_404.funco.trade.domain.QTrade.trade;

@RequiredArgsConstructor
@Repository
public class QueryDslTradeRepositoryImpl implements QueryDslTradeRepository {
    private final JPAQueryFactory jpaQueryFactory;


    @Override
    public List<Trade> findMyTradeHistoryByTicker(Long memberId, String ticker, Pageable pageable) {
        return jpaQueryFactory
                .selectFrom(trade)
                .where(trade.member.id.eq(memberId),
                        filterTicker(ticker))
                .orderBy(trade.updatedAt.desc())
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .fetch();
    }

    private Predicate filterTicker(String ticker) {
        return Objects.nonNull(ticker) ? trade.ticker.eq(ticker) : null;
    }

}
