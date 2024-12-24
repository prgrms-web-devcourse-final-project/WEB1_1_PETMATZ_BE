package com.petmatz.api.global;

import com.petmatz.api.global.dto.ImageErrorRequest;
import com.petmatz.api.global.dto.ImgType;
import com.petmatz.domain.pet.PetService;
import com.petmatz.domain.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class ImageErrorApi {
    private final UserService userService;
    private final PetService petService;

    @DeleteMapping("/image/error")
    @Operation(summary = "S3 이미지 오류시 객체를 삭제합니다.", description = "S3 이미지 오류시 회원삭제 API | [ 경고 ] 해당 API는 절대 사용하지 마세요")
    @Parameter(name = "id", description = "User ID", example = "1")
    public ResponseEntity<String> UserImageErrorDeleteUser(@RequestBody ImageErrorRequest imageErrorRequest) {

        selectService(imageErrorRequest.type(), imageErrorRequest.UUID());

        return ResponseEntity.ok("RollBack이 완료 되었습니다.");
    }

    private void selectService(ImgType type, Long UUID) {
        if (type.name().equals("U")) {
            userService.deleteUser(UUID);
        } else if (type.name().equals("P")) {

        } else if (type.name().equals("H")) {

        }
    }

}
