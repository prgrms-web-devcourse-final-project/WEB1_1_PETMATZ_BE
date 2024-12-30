package com.petmatz.domain.user.component;

import com.petmatz.domain.user.entity.User;
import com.petmatz.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserReader {

    private final UserRepository userRepository;

    public User getAuthenticatedUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found :" + userId));
    }

}
