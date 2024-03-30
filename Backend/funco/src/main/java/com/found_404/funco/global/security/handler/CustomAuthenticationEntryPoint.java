package com.found_404.funco.global.security.handler;

import java.io.IOException;

import org.json.simple.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.found_404.funco.global.security.exception.SecurityErrorCode;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
		AuthenticationException authException)
		throws IOException {
		SecurityErrorCode errorCode = (SecurityErrorCode)request.getAttribute("errorCode");

		HttpStatus httpStatus = (HttpStatus)request.getAttribute("httpStatus");
		setResponse(response, errorCode, httpStatus);
	}

	private void setResponse(HttpServletResponse response, SecurityErrorCode errorCode, HttpStatus httpStatus)
		throws IOException {

		response.setContentType("application/json;charset=UTF-8");
		response.setStatus(httpStatus.value());

		JSONObject responseJson = new JSONObject();
		responseJson.put("errorCode", errorCode.toString());
		responseJson.put("errorMessage", errorCode.getMessage());
		response.getWriter().print(responseJson);
	}
}