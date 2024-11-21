package com.petmatz.user.controller;

import com.petmatz.user.request.*;
import com.petmatz.user.response.*;
import com.petmatz.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class UserController {
    private final UserService userService;

    @PostMapping("/email-certification")
    public ResponseEntity<? super EmailCertificationResponseDto> emailCertification(@RequestBody @Valid EmailCertificationRequestDto requestBody) {
        ResponseEntity<? super EmailCertificationResponseDto> response= userService.emailCertification(requestBody);
        log.info("[emailCertification]: { accountId: " + requestBody.getAccountId() + "}");
        return response;
    }

    @PostMapping("/check-certification")
    public ResponseEntity<? super CheckCertificationResponseDto> checkCertification(@RequestBody @Valid CheckCertificationRequestDto requestBody) {
        ResponseEntity<? super CheckCertificationResponseDto> response= userService.checkCertification(requestBody);
        log.info("[checkCertification]: {accountId: " + requestBody.getAccountId() + ", certificationNumber: " + requestBody.getCertificationNumber() + "}");
        return response;
    }

    @PostMapping("/sign-up")
    public ResponseEntity<? super SignUpResponseDto> signUp(@RequestBody @Valid SignUpRequestDto requestBody) {
        ResponseEntity<? super SignUpResponseDto> response= userService.signUp(requestBody);
        log.info("[signUp]: { accountId: " + requestBody.getAccountId() + ", password: " + requestBody.getPassword());
        return response;
    }

    @PostMapping("/sign-in")
    public ResponseEntity<? super SignInResponseDto> signIn(@RequestBody @Valid SignInRequestDto requestBody) {
        ResponseEntity<? super SignInResponseDto> response= userService.signIn(requestBody);
        log.info("[signIn]: { accountId: " + requestBody.getAccountId() + ", password: " + requestBody.getPassword() + "}");
        return response;
    }

    @PostMapping("/delete-user")
    public ResponseEntity<? super DeleteIdResponseDto> deleteUser(@RequestBody @Valid DeleteIdRequestDto requestBody) {
        ResponseEntity<? super DeleteIdResponseDto> response= userService.deleteId(requestBody);
        log.info("[deleteUser]: {accountId: " + requestBody.getAccountId() + ", password: " + requestBody.getPassword() + "}");
        return response;
    }

    //---------------------------------------------------------------------------------------------------------------------------------//
    @GetMapping("/get-mypage")
    public ResponseEntity<? super GetMypageResponseDto> getMypage(@ModelAttribute @Valid GetMypageRequestDto requestBody) {
        ResponseEntity<? super GetMypageResponseDto> response = userService.getMypage(requestBody);
        log.info("[getMypage]");
        return response;
    }

    @PostMapping("/send-repassword")
    public ResponseEntity<? super SendRepasswordResponseDto> sendRepassword(@RequestBody @Valid SendRepasswordRequestDto requestBody) {
        ResponseEntity<? super SendRepasswordResponseDto> response = userService.sendRepassword(requestBody);
        log.info("[sendRepassword]: {accountId: " + requestBody.getAccountId()+"}");
        return response;
    }

    @PostMapping("/repassword")
    public ResponseEntity<? super RepasswordResponseDto> repassword(@RequestBody @Valid RepasswordRequestDto requestBody) {
        ResponseEntity<? super RepasswordResponseDto> response = userService.repassword(requestBody);
        log.info("[repassword]: {currentPassword: " + requestBody.getCurrentPassword() + ", newPassword: " + requestBody.getNewPassword() + "}");
        return response;
    }

}
