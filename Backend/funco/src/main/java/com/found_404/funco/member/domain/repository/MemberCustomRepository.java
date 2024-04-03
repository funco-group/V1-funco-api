package com.found_404.funco.member.domain.repository;

import java.util.List;

import com.found_404.funco.member.dto.MemberInfo;
import com.found_404.funco.trade.dto.HoldingCoinsDto;

public interface MemberCustomRepository {
	List<HoldingCoinsDto> findHoldingCoinsByMemberId(Long memberId);

	MemberInfo findMemberInfoByMemberId(Long memberId);

	Long getFollowingCashByMemberId(Long memberId);

	Long getFollowerCashByMemberId(Long memberId);

	Boolean isFollowedByMemberId(Long loginId, Long memberId);

	List<String> findRecentTradedCoinByMemberId(Long memberId);
}
