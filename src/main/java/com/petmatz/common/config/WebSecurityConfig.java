package com.petmatz.common.config;

import com.petmatz.common.security.filter.JwtAuthenticationFilter;
import com.petmatz.common.security.handler.OAuthSuccessHandler;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.io.IOException;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final DefaultOAuth2UserService oAuth2UserService;
    private final OAuthSuccessHandler oAuthSuccessHandler;

    @Bean
    protected SecurityFilterChain configure(HttpSecurity httpSecurity) throws Exception {

        httpSecurity
                .cors(cors -> cors.configurationSource(corsConfigurationSource())) // CORS 설정
                .csrf(CsrfConfigurer::disable) // CSRF 비활성화
                .httpBasic(HttpBasicConfigurer::disable) // HTTP Basic 인증 비활성화
                .sessionManagement(sessionManagement -> sessionManagement
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // 세션 비활성화 (JWT 사용)
                .authorizeHttpRequests(authRequests -> authRequests
                        .requestMatchers("/", "/api/auth/**", "/oauth2/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/sosboard").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/sosboard/user/{nickname}").permitAll()
                        .requestMatchers("/api/user/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")
                        //스웨거 모든 접근 허용
                        .requestMatchers(
                                "/swagger-ui/**",   
                                "/v3/api-docs/**",  
                                "/swagger-resources/**",
                                "/webjars/**"       
                        ).permitAll()
                        .anyRequest().authenticated()
                )
                .oauth2Login(oauth2 -> oauth2
                        .authorizationEndpoint(endpoint -> endpoint
                                .baseUri("/oauth2/authorization/kakao")) // OAuth2 로그인 엔드포인트
                        .redirectionEndpoint(endpoint -> endpoint
                                .baseUri("/oauth2/callback/*")) // 리디렉션 엔드포인트
                        .userInfoEndpoint(endpoint -> endpoint
                                .userService(oAuth2UserService)) // 사용자 정보 서비스
                        .successHandler(oAuthSuccessHandler) // OAuth2 성공 핸들러
                )
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .authenticationEntryPoint(new FailedAuthenticationEntryPoint())) // 인증 실패 핸들러
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class); // JWT 필터 추가

        return httpSecurity.build();
    }

    @Bean
    protected CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.addAllowedHeader("*");
        corsConfiguration.addAllowedMethod("*");
        corsConfiguration.setAllowCredentials(true);
        corsConfiguration.addAllowedOriginPattern("*"); // 모든 Origin 허용

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);
        return source;
    }
}

class FailedAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        System.out.println("req URI :: " + request.getRequestURI());
        System.out.println("request.getQueryString() :: " + request.getQueryString());
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_FORBIDDEN); // 403 상태 반환
        response.getWriter().write("{\"code\":\"NP\",\"message\":\"No Permission.\"}"); // 인증 실패 메시지
    }
}