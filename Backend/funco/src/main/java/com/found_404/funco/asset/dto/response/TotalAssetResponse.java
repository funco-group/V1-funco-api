package com.found_404.funco.asset.dto.response;

import com.found_404.funco.asset.dto.HoldingCoinInfo;
import lombok.Builder;

import java.util.List;

@Builder
public record TotalAssetResponse(
        Long cash,
        Long followingInvestment,
        List<HoldingCoinInfo> holdingCoinInfos
    ) {
}
