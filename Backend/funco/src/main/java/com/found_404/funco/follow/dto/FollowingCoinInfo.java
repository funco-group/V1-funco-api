package com.found_404.funco.follow.dto;

import com.found_404.funco.member.dto.MemberInfo;
import com.querydsl.core.annotations.QueryProjection;

public record FollowingCoinInfo(
	MemberInfo memberInfo,
	Long cash,
	Long followingAsset
) {

	@QueryProjection

	public FollowingCoinInfo(MemberInfo memberInfo, Long cash, Long followingAsset) {
		this.memberInfo = memberInfo;
		this.cash = cash;
		this.followingAsset = followingAsset;
	}
}
