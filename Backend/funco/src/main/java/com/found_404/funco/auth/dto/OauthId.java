package com.found_404.funco.auth.dto;

import com.found_404.funco.auth.type.OauthServerType;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OauthId {
	@Column(nullable = false, name = "oauth_server_id")
	private String oauthServerId;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, name = "oauth_server")
	private OauthServerType oauthServerType;

	@Builder
	public OauthId(String oauthServerId, OauthServerType oauthServerType) {
		this.oauthServerId = oauthServerId;
		this.oauthServerType = oauthServerType;
	}
}
