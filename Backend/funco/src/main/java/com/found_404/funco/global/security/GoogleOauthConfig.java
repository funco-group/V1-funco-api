package com.found_404.funco.global.security;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "spring.oauth.google")
public record GoogleOauthConfig(
	String redirectUri,
	String clientId,
	String clientSecretId,
	String[] scope
) {

}