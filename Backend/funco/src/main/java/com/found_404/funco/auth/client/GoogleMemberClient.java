package com.found_404.funco.auth.client;

import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import com.found_404.funco.auth.dto.GoogleToken;
import com.found_404.funco.auth.dto.OauthDto;
import com.found_404.funco.auth.dto.response.GoogleMemberResponse;
import com.found_404.funco.auth.exception.AuthErrorCode;
import com.found_404.funco.auth.exception.AuthException;
import com.found_404.funco.auth.type.OauthServerType;
import com.found_404.funco.global.security.GoogleOauthConfig;
import com.found_404.funco.member.domain.Member;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class GoogleMemberClient implements OauthMemberClient {
	private final GoogleApiClient googleApiClient;
	private final GoogleOauthConfig googleOauthConfig;

	@Override
	public OauthServerType supportServer() {
		return OauthServerType.GOOGLE;
	}

	@Override
	public OauthDto fetch(String authCode) {
		try {
			GoogleToken tokenInfo = googleApiClient.fetchToken(tokenRequestParams(authCode));
			GoogleMemberResponse googleMemberResponse =
				googleApiClient.fetchMember("Bearer " + tokenInfo.accessToken());
			Member member = googleMemberResponse.toDomain();
			return OauthDto.from(member, tokenInfo.accessToken());
		} catch (WebClientResponseException e) {
			throw new AuthException(AuthErrorCode.USED_AUTH_CODE);
		}
	}

	private MultiValueMap<String, String> tokenRequestParams(String authCode) {
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("grant_type", "authorization_code");
		params.add("client_id", googleOauthConfig.clientId());
		params.add("client_secret", googleOauthConfig.clientSecretId());
		params.add("redirect_uri", googleOauthConfig.redirectUri());
		params.add("code", authCode);
		return params;
	}
}