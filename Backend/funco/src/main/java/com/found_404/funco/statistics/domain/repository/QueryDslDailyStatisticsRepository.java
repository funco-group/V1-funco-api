package com.found_404.funco.statistics.domain.repository;

import java.util.List;

import com.found_404.funco.statistics.dto.response.DailyStatisticsResponse;

public interface QueryDslDailyStatisticsRepository {
	List<DailyStatisticsResponse> findDailyStatisticsByYearAndMonth(Integer year, Integer month);
}
