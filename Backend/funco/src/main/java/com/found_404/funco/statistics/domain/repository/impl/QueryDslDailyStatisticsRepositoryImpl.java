package com.found_404.funco.statistics.domain.repository.impl;

import static com.found_404.funco.statistics.domain.QDailyStatistics.*;

import java.util.List;

import com.found_404.funco.statistics.domain.repository.QueryDslDailyStatisticsRepository;
import com.found_404.funco.statistics.dto.response.DailyStatisticsResponse;
import com.found_404.funco.statistics.dto.response.QDailyStatisticsResponse;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class QueryDslDailyStatisticsRepositoryImpl implements QueryDslDailyStatisticsRepository {

	private final JPAQueryFactory jpaQueryFactory;

	@Override
	public List<DailyStatisticsResponse> findDailyStatisticsByYearAndMonth(Integer year, Integer month) {
		return jpaQueryFactory
			.select(new QDailyStatisticsResponse(dailyStatistics.date, dailyStatistics.returnResult,
				dailyStatistics.returnRate, dailyStatistics.accReturnResult, dailyStatistics.accReturnRate,
				dailyStatistics.beginningAsset, dailyStatistics.endingAsset))
			.from(dailyStatistics)
			.where(dailyStatistics.date.year().eq(year), dailyStatistics.date.month().eq(month))
			.orderBy(dailyStatistics.date.asc())
			.fetch();
	}
}
