package com.found_404.funco.auth.client;

import com.found_404.funco.auth.dto.OauthDto;
import com.found_404.funco.auth.type.OauthServerType;

public interface OauthMemberClient {
	OauthServerType supportServer();

	OauthDto fetch(String code);
}