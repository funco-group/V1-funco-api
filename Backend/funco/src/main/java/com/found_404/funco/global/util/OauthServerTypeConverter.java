package com.found_404.funco.global.util;

import org.springframework.core.convert.converter.Converter;

import com.found_404.funco.auth.type.OauthServerType;

public class OauthServerTypeConverter implements Converter<String, OauthServerType> {

	@Override
	public OauthServerType convert(String source) {
		return OauthServerType.fromName(source);
	}
}
