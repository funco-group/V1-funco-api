package com.found_404.funco.member.dto;

import com.querydsl.core.annotations.QueryProjection;

import lombok.Builder;

@Builder
public record MemberInfo(
	Long id,
	String nickname,
	String profileUrl
) {
	@QueryProjection
	public MemberInfo(Long id, String nickname, String profileUrl) {
		this.id = id;
		this.nickname = nickname;
		this.profileUrl = profileUrl;
	}
}
