package com.petmatz.domain.user.service;

public interface RePasswordEmailProvider {
    boolean sendVerificationEmail(String email, String rePassword);
}
