package com.petmatz.common.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class FailedAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        System.out.println("request.getRequestURI() :: " + request.getRequestURI());
        System.out.println("request.getQueryString() :: " + request.getQueryString());
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_FORBIDDEN); // 403 상태 반환
        response.getWriter().write("{\"code\":\"NP\",\"message\":\"No Permission.\"}"); // 인증 실패 메시지
    }

}
