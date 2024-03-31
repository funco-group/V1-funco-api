package com.found_404.funco.auth.type;

import static java.util.Locale.*;

public enum OauthServerType {
	GOOGLE,
	;

	public static OauthServerType fromName(String type) {
		return OauthServerType.valueOf(type.toUpperCase(ENGLISH));
	}
}
