package com.found_404.funco.auth.authCode;

import com.found_404.funco.auth.type.OauthServerType;

public interface AuthCodeRequestUrlProvider {
	OauthServerType supportServer();

	String provide();
}
