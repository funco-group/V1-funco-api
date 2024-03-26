package com.found_404.funco.statistics.domain.repository.impl;

import static com.found_404.funco.statistics.domain.QMonthlyStatistics.*;

import java.util.List;

import com.found_404.funco.statistics.domain.repository.QueryDslMonthlyStatisticsRepository;
import com.found_404.funco.statistics.dto.response.MonthlyStatisticsResponse;
import com.found_404.funco.statistics.dto.response.QMonthlyStatisticsResponse;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class QueryDslMonthlyStatisticsRepositoryImpl implements QueryDslMonthlyStatisticsRepository {

	private final JPAQueryFactory jpaQueryFactory;

	@Override
	public List<MonthlyStatisticsResponse> findMonthlyStatisticsByYear(Long memberId, Integer year) {
		return jpaQueryFactory.select(
				new QMonthlyStatisticsResponse(monthlyStatistics.date.month(), monthlyStatistics.returnResult,
					monthlyStatistics.returnRate,
					monthlyStatistics.accReturnResult, monthlyStatistics.accReturnRate, monthlyStatistics.beginningAsset,
					monthlyStatistics.endingAsset))
			.from(monthlyStatistics)
			.where(monthlyStatistics.member.id.eq(memberId).and(monthlyStatistics.date.year().eq(year)))
			.orderBy(monthlyStatistics.date.asc())
			.fetch();
	}
}
