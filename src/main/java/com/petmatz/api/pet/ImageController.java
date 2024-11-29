package com.petmatz.api.pet;

import com.petmatz.api.global.dto.Response;
<<<<<<< HEAD
import com.petmatz.domain.pet.PetService;
=======
import com.petmatz.domain.pet.PetServiceImpl;
>>>>>>> 6f950d720c5e24af49fee2d9bc095cf2ca4a965c
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

import java.util.Map;

@RestController
@RequestMapping("/api/images")
@RequiredArgsConstructor
@Tag(name = "Image API", description = "이미지 관리 API")
public class ImageController {

    private final PetServiceImpl petServiceImpl;

    // 이미지 업로드
    @PostMapping("/upload")
    @Operation(summary = "이미지 업로드", description = "이미지를 업로드하고 저장 경로를 반환합니다.")
    @Parameter(name = "file", description = "업로드할 이미지 파일", required = true, schema = @Schema(type = "string", format = "binary"))
    public ResponseEntity<Response<Map<String, String>>> uploadImage(@RequestParam("file") MultipartFile file) {
        Map<String, String> response = petServiceImpl.uploadImage(file);
        return ResponseEntity.ok(Response.success(response));
    }
}


