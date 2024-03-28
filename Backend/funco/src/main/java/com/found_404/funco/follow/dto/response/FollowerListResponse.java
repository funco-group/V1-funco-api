package com.found_404.funco.follow.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import com.querydsl.core.annotations.QueryProjection;

import lombok.Builder;

@Builder
public record FollowerListResponse(
	Boolean last,
	List<FollowerInfo> followers
) {
	@Builder
	public record FollowerInfo(
		Long followId,
		LocalDateTime followedAt,
		String nickname,
		Long investment,
		Long settlement,
		Double returnRate,
		Long commission,
		LocalDateTime settleDate

	) {
		@QueryProjection
		public FollowerInfo(Long followId, LocalDateTime followedAt, String nickname, Long investment, Long settlement,
			Double returnRate, Long commission, LocalDateTime settleDate) {
			this.followId = followId;
			this.followedAt = followedAt;
			this.nickname = nickname;
			this.investment = investment;
			this.settlement = settlement;
			this.returnRate = returnRate;
			this.commission = commission;
			this.settleDate = settleDate;
		}
	}
}
