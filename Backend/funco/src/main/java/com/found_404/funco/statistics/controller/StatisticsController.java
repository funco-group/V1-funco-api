package com.found_404.funco.statistics.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.found_404.funco.global.util.AuthMemberId;
import com.found_404.funco.statistics.dto.response.DailyStatisticsResponse;
import com.found_404.funco.statistics.dto.response.MonthlyStatisticsResponse;
import com.found_404.funco.statistics.dto.response.StartDateResponse;
import com.found_404.funco.statistics.service.StatisticsService;

import lombok.RequiredArgsConstructor;

@RequestMapping("/v1/statistics")
@RequiredArgsConstructor
@RestController
public class StatisticsController {

	private final StatisticsService statisticsService;

	@GetMapping("/daily")
	public ResponseEntity<List<DailyStatisticsResponse>> getDailyStatistics(@AuthMemberId Long memberId,
		@RequestParam Integer year,
		@RequestParam Integer month) {
		return ResponseEntity.status(HttpStatus.OK).body(statisticsService.readDailyStatistics(memberId, year, month));
	}

	@GetMapping("/monthly")
	public ResponseEntity<List<MonthlyStatisticsResponse>> getMonthlyStatistics(@AuthMemberId Long memberId,
		@RequestParam Integer year) {
		return ResponseEntity.status(HttpStatus.OK).body(statisticsService.readMonthlyStatistics(memberId, year));
	}

	@GetMapping("/startdate")
	public ResponseEntity<StartDateResponse> getStartDate(@AuthMemberId Long memberId) {
		return ResponseEntity.status(HttpStatus.OK).body(statisticsService.readStartDate(memberId));
	}

	@GetMapping("/daily/{memberId}")
	public ResponseEntity<List<DailyStatisticsResponse>> getOtherDailyStatistics(@PathVariable Long memberId,
		@RequestParam Integer year,
		@RequestParam Integer month) {
		return ResponseEntity.status(HttpStatus.OK).body(statisticsService.readDailyStatistics(memberId, year, month));
	}

	@GetMapping("/monthly/{memberId}")
	public ResponseEntity<List<MonthlyStatisticsResponse>> getOtherMonthlyStatistics(@PathVariable Long memberId,
		@RequestParam Integer year) {
		return ResponseEntity.status(HttpStatus.OK).body(statisticsService.readMonthlyStatistics(memberId, year));
	}

	@GetMapping("/startdate/{memberId}")
	public ResponseEntity<StartDateResponse> getOtherStartDate(@PathVariable Long memberId) {
		return ResponseEntity.status(HttpStatus.OK).body(statisticsService.readStartDate(memberId));
	}
}
