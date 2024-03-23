package com.found_404.funco.statistics.dto.response;

import com.querydsl.core.annotations.QueryProjection;

public record MonthlyStatisticsResponse(
	Integer month,
	Long returnResult,
	Double returnRate,
	Long accReturnResult,
	Double accReturnRate,
	Long beginningAsset,
	Long endingAsset
) {

	@QueryProjection
	public MonthlyStatisticsResponse(Integer month, Long returnResult, Double returnRate, Long accReturnResult,
		Double accReturnRate, Long beginningAsset, Long endingAsset) {
		this.month = month;
		this.returnResult = returnResult;
		this.returnRate = returnRate;
		this.accReturnResult = accReturnResult;
		this.accReturnRate = accReturnRate;
		this.beginningAsset = beginningAsset;
		this.endingAsset = endingAsset;
	}
}
