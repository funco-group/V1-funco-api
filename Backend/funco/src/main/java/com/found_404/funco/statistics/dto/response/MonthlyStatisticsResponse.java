package com.found_404.funco.statistics.dto.response;

import java.time.LocalDate;

public record MonthlyStatisticsResponse(
	LocalDate date,
	Long returnResult,
	Double returnRate,
	Long accReturnResult,
	Double accReturnRate,
	Long beginningAsset,
	Long endingAsset
) {
}
