package com.found_404.funco.global.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsUtils;

import com.found_404.funco.global.security.filter.JwtAuthenticationFilter;
import com.found_404.funco.global.security.handler.CustomAuthenticationEntryPoint;
import com.found_404.funco.global.security.service.TokenService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
public class SecurityConfig {

	private final TokenService tokenService;

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.csrf(AbstractHttpConfigurer::disable)
			.cors(AbstractHttpConfigurer::disable)
			.sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
			.formLogin(AbstractHttpConfigurer::disable)
			.authorizeHttpRequests(auth -> auth.requestMatchers(CorsUtils::isPreFlightRequest).permitAll())
			.authorizeHttpRequests(auth -> auth.requestMatchers("/v1/auth/*/signin", "/v1/rank")
				.permitAll()
				// .requestMatchers("/**")
				// .permitAll()
				.anyRequest()
				.authenticated())

		;

		http.addFilterBefore(new JwtAuthenticationFilter(tokenService), UsernamePasswordAuthenticationFilter.class);
		http.exceptionHandling(e -> e.authenticationEntryPoint(new CustomAuthenticationEntryPoint()));

		return http.build();
	}
}
