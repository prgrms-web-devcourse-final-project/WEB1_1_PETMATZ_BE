package com.petmatz.common.security.filter;

import com.petmatz.domain.user.entity.User;
import com.petmatz.common.security.utils.JwtProvider;
import com.petmatz.domain.user.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
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

            String accountId = jwtProvider.validate(token);
            if (accountId == null) {
                filterChain.doFilter(request, response);
                return;
            }

            // 사용자 ID로 사용자 정보 조회
            User userEntity = userRepository.findByAccountId(accountId);
            User.LoginRole loginRole = userEntity.getLoginRole();

            // 사용자 권한 설정
            List<GrantedAuthority> authorities = new ArrayList<>();
            authorities.add(new SimpleGrantedAuthority(String.valueOf(loginRole)));

            // 인증 객체 생성 및 SecurityContext에 설정
            SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
            AbstractAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(accountId, null, authorities);
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            securityContext.setAuthentication(authenticationToken);
            SecurityContextHolder.setContext(securityContext);

        } catch (Exception e) {
            log.info("Invalid JWT token: {}", e.getMessage());
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