package com.found_404.funco.follow.dto;

import java.util.List;

import lombok.Builder;

@Builder
public record SliceFollowingInfo(
	List<FollowingInfo> followingInfoList,
	Boolean last
) {
}
