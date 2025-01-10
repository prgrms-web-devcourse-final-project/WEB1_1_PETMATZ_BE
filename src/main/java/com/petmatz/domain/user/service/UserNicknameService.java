package com.petmatz.domain.user.service;

import com.petmatz.common.security.utils.JwtExtractProvider;
import com.petmatz.domain.user.repository.UserRepository;
import com.petmatz.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserNicknameService {
    private final UserRepository userRepository;
    private final JwtExtractProvider jwtExtractProvider;

    public String getNicknameByEmail() {
        String accountId = jwtExtractProvider.findAccountIdFromJwt();
        User user = userRepository.findByAccountId(accountId);
        if (user == null) {
            return "Unknown User"; // 예외 처리
        }
        return user.getNickname(); // 닉네임 반환
    }
}
