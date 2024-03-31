package com.found_404.funco.global.security.handler;

import java.io.IOException;

import org.json.simple.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response,
		AccessDeniedException accessDeniedException) throws IOException, ServletException {
		response.setContentType("application/json;charset=UTF-8");
		response.setStatus(HttpStatus.FORBIDDEN.value());

		JSONObject responseJson = new JSONObject();
		responseJson.put("errorCode", "FORBIDDEN_ACCESS");
		responseJson.put("errorMessage", "허용되지 않은 접근입니다.");
		response.getWriter().print(responseJson);
	}
}
