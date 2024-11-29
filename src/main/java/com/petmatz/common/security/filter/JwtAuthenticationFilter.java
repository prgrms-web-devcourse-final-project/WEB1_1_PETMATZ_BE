package com.petmatz.common.security.filter;

import com.petmatz.domain.user.constant.LoginRole;
import com.petmatz.domain.user.entity.User;
import com.petmatz.common.security.utils.JwtProvider;
import com.petmatz.domain.user.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * JWT 인증 필터 클래스.
 * 쿠키에서 JWT를 읽어 인증 처리.
 */
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            // 쿠키에서 JWT 토큰 추출
            String token = getJwtFromCookies(request);

            if (token == null) {
                filterChain.doFilter(request, response);
                return;
            }

            // JWT 유효성 검증 및 사용자 ID 추출
            Long userId = jwtProvider.validateAndGetUserId(token); // validate 메서드가 userId를 반환하도록 수정
            if (userId == null) {
                filterChain.doFilter(request, response);
                return;
            }

            // 사용자 ID로 사용자 정보 조회
            User userEntity = userRepository.findById(userId).orElse(null);
            if (userEntity == null) {
                filterChain.doFilter(request, response);
                return;
            }

            // 사용자 권한 설정
            LoginRole loginRole = userEntity.getLoginRole();
            List<GrantedAuthority> authorities = new ArrayList<>();
            authorities.add(new SimpleGrantedAuthority(String.valueOf(loginRole)));

            // 인증 객체 생성 및 SecurityContext에 설정
            SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
            AbstractAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(userId, null, authorities); // userId를 principal로 설정
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            securityContext.setAuthentication(authenticationToken);
            SecurityContextHolder.setContext(securityContext);

        } catch (Exception e) {
//            log.error("JWT 인증 실패", e);
        }

        filterChain.doFilter(request, response);
    }

    /**
     * 쿠키에서 JWT 추출
     */
    private String getJwtFromCookies(HttpServletRequest request) {
        if (request.getCookies() == null) return null;

        Optional<Cookie> jwtCookie = Arrays.stream(request.getCookies())
                .filter(cookie -> "jwt".equals(cookie.getName())) // 쿠키 이름을 "jwt"로 가정
                .findFirst();

        return jwtCookie.map(Cookie::getValue).orElse(null);
    }


}