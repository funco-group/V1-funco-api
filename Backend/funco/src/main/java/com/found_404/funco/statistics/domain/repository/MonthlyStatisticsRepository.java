package com.found_404.funco.statistics.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.found_404.funco.statistics.domain.MonthlyStatistics;

public interface MonthlyStatisticsRepository extends JpaRepository<MonthlyStatistics, Long> {
	
}
