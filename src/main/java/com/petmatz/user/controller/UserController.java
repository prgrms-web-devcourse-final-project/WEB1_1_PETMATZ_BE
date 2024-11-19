package com.petmatz.user.controller;

import com.petmatz.user.request.*;
import com.petmatz.user.response.*;
import com.petmatz.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class UserController {
    private final UserService userService;

    @PostMapping("/email-certification")
    public ResponseEntity<? super EmailCertificationResponseDto> emailCertification(@RequestBody @Valid EmailCertificationRequestDto requestBody) {
        ResponseEntity<? super EmailCertificationResponseDto> response= userService.emailCertification(requestBody);
        log.info("[emailCertification]: { email: " + requestBody.getAccountId() + "}");
        return response;
    }

    @PostMapping("/check-certification")
    public ResponseEntity<? super CheckCertificationResponseDto> checkCertification(@RequestBody @Valid CheckCertificationRequestDto requestBody) {
        ResponseEntity<? super CheckCertificationResponseDto> response= userService.checkCertification(requestBody);
        log.info("[checkCertification]: {email: " + requestBody.getAccountId() + ", certificationNumber: " + requestBody.getCertificationNumber() + "}");
        return response;
    }

    @PostMapping("/sign-up")
    public ResponseEntity<? super SignUpResponseDto> signUp(@RequestBody @Valid SignUpRequestDto requestBody) {
        ResponseEntity<? super SignUpResponseDto> response= userService.signUp(requestBody);
        log.info("[signUp]: { id: " + requestBody.getAccountId() + ", password: " + requestBody.getPassword());
        return response;
    }

    @PostMapping("/sign-in")
    public ResponseEntity<? super SignInResponseDto> signIn(@RequestBody @Valid SignInRequestDto requestBody) {
        ResponseEntity<? super SignInResponseDto> response= userService.signIn(requestBody);
        log.info("[signIn]: { id: " + requestBody.getAccountId() + ", password: " + requestBody.getPassword() + "}");
        return response;
    }

    @PostMapping("/delete-user")
    public ResponseEntity<? super DeleteIdResponseDto> deleteUser(@RequestBody @Valid DeleteIdRequestDto requestBody) {
        ResponseEntity<? super DeleteIdResponseDto> response= userService.deleteId(requestBody);
        log.info("[deleteUser]: {id: " + requestBody.getAccountId() + ", password: " + requestBody.getPassword() + "}");
        return response;
    }

}
