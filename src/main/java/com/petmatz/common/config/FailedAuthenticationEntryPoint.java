package com.petmatz.common.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.naming.AuthenticationException;
import java.io.IOException;

/**
 * 인증 실패 시 처리하는 엔트리 포인트 클래스.
 * 인증되지 않은 요청이 들어올 경우 403 Forbidden 상태와 JSON 메시지를 응답으로 반환.
 */
public class FailedAuthenticationEntryPoint implements AuthenticationEntryPoint {

    /**
     * 인증 실패 시 호출되는 메서드.
     */

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, org.springframework.security.core.AuthenticationException authException) throws IOException, ServletException {
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_FORBIDDEN); // 403 상태 반환
        response.getWriter().write("{\"code\":\"NP\",\"message\":\"No Permission.\"}"); // 인증 실패 메시지 반환
    }
}
