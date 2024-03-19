package com.found_404.funco.statistics.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.found_404.funco.statistics.domain.DailyStatistics;

public interface DailyStatisticsRepository extends JpaRepository<DailyStatistics, Long> {
}
