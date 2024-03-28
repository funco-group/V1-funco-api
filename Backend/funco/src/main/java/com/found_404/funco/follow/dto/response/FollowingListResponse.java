package com.found_404.funco.follow.dto.response;

import java.util.List;

import com.found_404.funco.follow.dto.FollowingInfo;

import lombok.Builder;

@Builder
public record FollowingListResponse(

	Long totalAsset,
	List<FollowingInfo> followings,
	Boolean last
) {

}
