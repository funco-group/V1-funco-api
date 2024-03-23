package com.found_404.funco.statistics.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.found_404.funco.statistics.dto.response.DailyStatisticsResponse;
import com.found_404.funco.statistics.dto.response.MonthlyStatisticsResponse;
import com.found_404.funco.statistics.service.StatisticsService;

import lombok.RequiredArgsConstructor;

@RequestMapping("/statistics")
@RequiredArgsConstructor
@RestController
public class StatisticsController {

	private final StatisticsService statisticsService;

	@GetMapping("/daily")
	public ResponseEntity<List<DailyStatisticsResponse>> getDailyStatistics(@RequestParam Integer year,
		@RequestParam Integer month) {
		return ResponseEntity.status(HttpStatus.OK).body(statisticsService.readDailyStatistics(year, month));
	}

	@GetMapping("/monthly")
	public ResponseEntity<List<MonthlyStatisticsResponse>> getMonthlyStatistics(@RequestParam Integer year) {
		return ResponseEntity.status(HttpStatus.OK).body(statisticsService.readMonthlyStatistics(year));
	}
}
