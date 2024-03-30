package com.found_404.funco.global.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.found_404.funco.global.util.OauthServerTypeConverter;

@Configuration
public class CorsConfig implements WebMvcConfigurer {
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**")
			.allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
			.allowedOrigins("http://localhost:3000", "http://192.168.229.28:5173", "http://192.168.229.28:5173/",
				"http://localhost:5173")
			.allowedHeaders("Content-Type", "Authorization") // Authorization 헤더 허용
			.allowCredentials(true)
			.maxAge(3600)
			.exposedHeaders("*");
	}

	@Override
	public void addFormatters(FormatterRegistry registry) {
		registry.addConverter(new OauthServerTypeConverter());
	}
}
