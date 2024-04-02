package com.found_404.funco.member.dto;

import com.querydsl.core.annotations.QueryProjection;

import lombok.Builder;

@Builder
public record MemberInfo(
	Long id,
	String nickname,
	String profileUrl,
	String introduction,
	Long cash
) {
	@QueryProjection
	public MemberInfo(Long id, String nickname, String profileUrl) {
		this(id, nickname, profileUrl, "", null);
	}

	@QueryProjection
	public MemberInfo(String nickname, String profileUrl, String introduction, Long cash) {
		this(null, nickname, profileUrl, introduction, cash);
	}

	public MemberInfo(Long id, String nickname, String profileUrl, String introduction, Long cash) {
		this.id = id;
		this.nickname = nickname;
		this.profileUrl = profileUrl;
		this.introduction = introduction;
		this.cash = cash;
	}
}
