package com.found_404.funco.member.dto.response;

import java.util.List;

import com.found_404.funco.member.dto.MemberAssetInfo;

import lombok.Builder;

@Builder
public record MemberResponse(
	Long memberId,
	String nickname,
	String profileUrl,
	String introduction,
	Long assetRank,
	Long followingCashRank,
	MemberAssetInfo memberAssetInfo,
	List<String> topCoins,
	Long followingCash,
	Long followerCash,
	Boolean isFollow
) {
}
