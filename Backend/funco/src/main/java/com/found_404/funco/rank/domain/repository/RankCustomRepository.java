package com.found_404.funco.rank.domain.repository;

import java.util.List;

import com.found_404.funco.follow.dto.FollowingCoinInfo;
import com.found_404.funco.trade.dto.HoldingCoinInfo;

public interface RankCustomRepository {
	List<HoldingCoinInfo> findHoldingCoinInfo();

	List<FollowingCoinInfo> findFollowingCoinInfo();

	List<String> findHoldingCoin();

	Long getInvestmentByMemberId(Long memberId);
}
