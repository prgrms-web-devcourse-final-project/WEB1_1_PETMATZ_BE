package com.petmatz.common.security.utils;

import jakarta.servlet.http.HttpServletRequest;

public interface JwtExtractProvider {
    Long findIdFromJwt();
    String findAccountIdFromJwt();
}
