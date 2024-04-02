package com.found_404.funco.member.dto;

import java.util.List;

import com.found_404.funco.trade.dto.HoldingCoinsDto;

import lombok.Builder;

@Builder
public record MemberAssetInfo(
	Long cash,
	List<HoldingCoinsDto> coins
) {
}
