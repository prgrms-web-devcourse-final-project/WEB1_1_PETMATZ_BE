package com.petmatz.domain.user.service;

public interface EmailProvider {
    boolean sendVerificationEmail(String email, String certificationNumber);
}
