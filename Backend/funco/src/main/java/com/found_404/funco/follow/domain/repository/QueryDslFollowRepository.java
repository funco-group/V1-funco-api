package com.found_404.funco.follow.domain.repository;

import java.util.List;

import com.found_404.funco.follow.dto.SliceFollowingInfo;
import com.found_404.funco.follow.dto.response.FollowerListResponse;

public interface QueryDslFollowRepository {

	SliceFollowingInfo findFollowingInfoListByMemberId(Long memberId, Long lastFollowId, int pageSize);

	List<String> findHoldingCoin(Long memberId);

	FollowerListResponse findFollowerListByMemberIdAndSettleType(Long memberId, String settleType,
		Long lastFollowId,
		int pageSize);
}
