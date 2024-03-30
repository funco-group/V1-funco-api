package com.found_404.funco.global.security.filter;

import static com.found_404.funco.global.security.exception.SecurityErrorCode.*;
import static org.springframework.http.HttpStatus.*;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import com.found_404.funco.global.security.exception.SecurityException;
import com.found_404.funco.global.security.service.TokenService;

import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends GenericFilterBean {

	private final TokenService tokenService;

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
		throws IOException, ServletException {
		try {
			String token = tokenService.resolveToken((HttpServletRequest)request).replace("Bearer ", "");
			if (tokenService.validateToken(token)) { // access_token 유효할 때
				Authentication authentication = tokenService.readAuthentication(token);
				SecurityContextHolder.getContext().setAuthentication(authentication);
			} else { // access_token 유효하지 않을 때 재발급
				String accessToken =
					tokenService.reissueAccessToken((HttpServletRequest)request,
						(HttpServletResponse)response).accessToken();
				((HttpServletResponse)response).setHeader("Authorization", accessToken);
				Authentication authentication = tokenService.readAuthentication(accessToken);
				SecurityContextHolder.getContext().setAuthentication(authentication);
			}
		} catch (SecurityException e) {
			request.setAttribute("errorCode", e.getErrorCode());
			request.setAttribute("httpStatus", e.getHttpStatus());
		} catch (SignatureException | MalformedJwtException e) {
			request.setAttribute("errorCode", INVALID_TOKEN);
			request.setAttribute("httpStatus", UNAUTHORIZED);
		} catch (IllegalArgumentException e) {
			request.setAttribute("errorCode", INVALID_TOKEN);
			request.setAttribute("httpStatus", BAD_REQUEST);
		} catch (Exception e) {
			e.printStackTrace();
		}

		chain.doFilter(request, response);
	}
}
