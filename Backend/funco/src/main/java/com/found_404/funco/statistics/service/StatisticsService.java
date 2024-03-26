package com.found_404.funco.statistics.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.found_404.funco.statistics.domain.repository.DailyStatisticsRepository;
import com.found_404.funco.statistics.domain.repository.MonthlyStatisticsRepository;
import com.found_404.funco.statistics.dto.response.DailyStatisticsResponse;
import com.found_404.funco.statistics.dto.response.MonthlyStatisticsResponse;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class StatisticsService {

	private final DailyStatisticsRepository dailyStatisticsRepository;
	private final MonthlyStatisticsRepository monthlyStatisticsRepository;

	public List<DailyStatisticsResponse> readDailyStatistics(Long memberId, Integer year, Integer month) {
		return dailyStatisticsRepository.findDailyStatisticsByYearAndMonth(memberId, year, month);
	}

	public List<MonthlyStatisticsResponse> readMonthlyStatistics(Long memberId, Integer year) {
		return monthlyStatisticsRepository.findMonthlyStatisticsByYear(memberId, year);
	}
}
