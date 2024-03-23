package com.found_404.funco.statistics.dto.response;

import java.time.LocalDate;

import com.querydsl.core.annotations.QueryProjection;

import lombok.Builder;

@Builder
public record DailyStatisticsResponse(
	LocalDate date,
	Long returnResult,
	Double returnRate,
	Long accReturnResult,
	Double accReturnRate,
	Long beginningAsset,
	Long endingAsset
) {

	@QueryProjection
	public DailyStatisticsResponse(LocalDate date, Long returnResult, Double returnRate, Long accReturnResult,
		Double accReturnRate, Long beginningAsset, Long endingAsset) {
		this.date = date;
		this.returnResult = returnResult;
		this.returnRate = returnRate;
		this.accReturnResult = accReturnResult;
		this.accReturnRate = accReturnRate;
		this.beginningAsset = beginningAsset;
		this.endingAsset = endingAsset;
	}

}
