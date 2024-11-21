package com.petmatz.user.handler;

import com.petmatz.user.entity.CustomOAuthUser;
import com.petmatz.user.entity.User;
import com.petmatz.user.provider.JwtProvider;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * OAuth2 인증 성공 시 호출되는 핸들러 클래스.
 * JWT 토큰을 생성하고 클라이언트로 응답을 처리하는 역할
 */
@Component
@RequiredArgsConstructor
public class OAuthSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    // JWT 토큰을 생성하는 프로바이더
    private final JwtProvider jwtProvider;

    /**
     * OAuth2 인증이 성공했을 때 호출되는 메서드.
     * 사용자 정보를 바탕으로 JWT 토큰을 생성하고 클라이언트로 전달.
     *
     * @param authentication 인증 객체 (OAuth2 사용자 정보 포함)
     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        // 인증된 OAuth2 사용자 정보 가져오기
        CustomOAuthUser oAuth2User = (CustomOAuthUser) authentication.getPrincipal();

        // 사용자 ID를 Long 타입으로 가져오기
        String accountId = oAuth2User.getName();
        String token = jwtProvider.create(accountId, User.LoginRole.ROLE_USER);

        // 인증 성공 후 클라이언트로 토큰을 전달할 수 있는 로직 추가 (리다이렉트)
        response.sendRedirect("http://localhost:3000/auth/oauth-response/" + token + "/3600");

    }
}