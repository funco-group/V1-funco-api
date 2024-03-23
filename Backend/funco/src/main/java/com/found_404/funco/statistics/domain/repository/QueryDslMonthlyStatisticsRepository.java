package com.found_404.funco.statistics.domain.repository;

import java.util.List;

import com.found_404.funco.statistics.dto.response.MonthlyStatisticsResponse;

public interface QueryDslMonthlyStatisticsRepository {
	List<MonthlyStatisticsResponse> findMonthlyStatisticsByYear(Integer year);
}
