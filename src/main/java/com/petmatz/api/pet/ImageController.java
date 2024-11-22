package com.petmatz.api.pet;

import com.petmatz.api.global.dto.Response;
import com.petmatz.domain.pet.PetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/api/images")
@RequiredArgsConstructor
public class ImageController {

    private final PetService petService;

    // 이미지 업로드
    @PostMapping("/upload")
    public ResponseEntity<Response<Map<String, String>>> uploadImage(@RequestParam("file") MultipartFile file) {
        Map<String, String> response = petService.uploadImage(file);
        return ResponseEntity.ok(Response.success(response));
    }
}


