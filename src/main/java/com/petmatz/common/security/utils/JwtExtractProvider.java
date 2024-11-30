package com.petmatz.common.security.utils;


public interface JwtExtractProvider {
    Long findIdFromJwt();
    String findAccountIdFromJwt();
}
