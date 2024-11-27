package com.petmatz.common.security.utils;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class JwtExtractProviderImpl implements JwtExtractProvider{

    @Override
    public String findAccountIdFromJwt() {
        String accountId = "Default_Id";
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            accountId = authentication.getName();
        }
        return accountId;
    }
}
