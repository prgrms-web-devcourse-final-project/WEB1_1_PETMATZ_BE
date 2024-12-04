package com.petmatz.api.global;

import com.petmatz.api.global.dto.ImageErrorRequest;
import com.petmatz.domain.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ImageErrorApi {
    private final UserService userService;

    @DeleteMapping("/image/error")
    @Operation(summary = "S3 이미지 오류시 회원을 삭제합니다.", description = "S3 이미지 오류시 회원삭제 API")
    @Parameter(name = "id", description = "User ID", example = "1")
    public ResponseEntity<String> UserImageErrorDeleteUser(@RequestBody ImageErrorRequest imageErrorRequest) {
        userService.deleteUser(imageErrorRequest.UUID());
        return ResponseEntity.ok("RollBack이 완료 되었습니다.");
    }

}
