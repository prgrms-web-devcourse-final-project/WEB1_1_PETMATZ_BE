package com.petmatz.domain.user.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;

/**
 * OAuth2User 커스텀을 위한 핸들
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CustomOAuthUser implements OAuth2User {

    private Long userId; // Long 타입의 사용자 ID
    private String accountId; // accountId 필드 추가
    private Map<String, Object> attributes; // OAuth2 사용자 속성
    private Collection<? extends GrantedAuthority> authorities; // 사용자 권한

    @Override
    public Map<String, Object> getAttributes() {
        return this.attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getName() {
        return this.accountId; // accountId를 반환
    }
}