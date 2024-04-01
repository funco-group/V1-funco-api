package com.found_404.funco.statistics.domain.repository.impl;

import static com.found_404.funco.statistics.domain.QDailyStatistics.*;

import java.util.List;

import com.found_404.funco.statistics.domain.repository.QueryDslDailyStatisticsRepository;
import com.found_404.funco.statistics.dto.response.DailyStatisticsResponse;
import com.found_404.funco.statistics.dto.response.QDailyStatisticsResponse;
import com.found_404.funco.statistics.dto.response.StartDateResponse;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class QueryDslDailyStatisticsRepositoryImpl implements QueryDslDailyStatisticsRepository {

	private final JPAQueryFactory jpaQueryFactory;

	@Override
	public List<DailyStatisticsResponse> findDailyStatisticsByYearAndMonth(Long memberId, Integer year, Integer month) {
		return jpaQueryFactory
			.select(new QDailyStatisticsResponse(dailyStatistics.date, dailyStatistics.returnResult,
				dailyStatistics.returnRate, dailyStatistics.accReturnResult, dailyStatistics.accReturnRate,
				dailyStatistics.beginningAsset, dailyStatistics.endingAsset))
			.from(dailyStatistics)
			.where(dailyStatistics.member.id.eq(memberId)
				.and(dailyStatistics.date.year().eq(year))
				.and(dailyStatistics.date.month().eq(month)))
			.orderBy(dailyStatistics.date.asc())
			.fetch();
	}

	@Override
	public StartDateResponse findStartDateByMemberId(Long memberId) {
		return jpaQueryFactory
			.select(Projections.constructor(StartDateResponse.class, dailyStatistics.date.year(),
				dailyStatistics.date.month()))
			.from(dailyStatistics)
			.where(dailyStatistics.member.id.eq(memberId))
			.fetchFirst();
	}
}