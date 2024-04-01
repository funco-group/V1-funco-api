package com.found_404.funco.statistics.domain.repository;

import java.util.List;

import com.found_404.funco.statistics.dto.response.DailyStatisticsResponse;
import com.found_404.funco.statistics.dto.response.StartDateResponse;

public interface QueryDslDailyStatisticsRepository {
	List<DailyStatisticsResponse> findDailyStatisticsByYearAndMonth(Long memberId, Integer year, Integer month);

	StartDateResponse findStartDateByMemberId(Long memberId);
}
