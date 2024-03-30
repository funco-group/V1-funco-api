package com.found_404.funco.auth.dto.response;

import static com.found_404.funco.auth.type.OauthServerType.*;

import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.found_404.funco.auth.dto.OauthId;
import com.found_404.funco.member.domain.Member;

@JsonNaming(SnakeCaseStrategy.class)
public record GoogleMemberResponse(
	String id,
	String email,
	String verified_email,
	String name,
	String given_name,
	String family_name,
	String picture,
	String local
) {
	public Member toDomain() {
		return Member.builder()
			.oauthId(OauthId.builder()
				.oauthServerId(id)
				.oauthServerType(GOOGLE)
				.build())
			.nickname(name)
			.profileUrl(picture)
			.build();
	}
}