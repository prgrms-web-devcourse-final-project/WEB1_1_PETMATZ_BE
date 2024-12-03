package com.petmatz.domain.pet;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.petmatz.api.pet.dto.PetInfoDto;
import com.petmatz.domain.aws.AwsClient;
import com.petmatz.domain.pet.dto.PetServiceDto;
import com.petmatz.domain.pet.dto.PetServiceDtoFactory;
import com.petmatz.domain.pet.exception.ImageErrorCode;
import com.petmatz.domain.pet.exception.ImageServiceException;
import com.petmatz.domain.pet.exception.PetErrorCode;
import com.petmatz.domain.pet.exception.PetServiceException;
import com.petmatz.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PetServiceImpl implements PetService{

    private final AwsClient awsClient;

    private final PetRepository repository;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final String uploadDir = System.getProperty("user.dir") + "/uploads/";

    // 동물 등록 정보를 외부 API로 가져오는 메서드
    public PetServiceDto fetchPetInfo(String dogRegNo, String ownerNm) {
        if (repository.existsByDogRegNo(dogRegNo)) {
            throw new PetServiceException(PetErrorCode.DOG_REG_NO_DUPLICATE);
        }

        try {
            String response = callApi(dogRegNo, ownerNm);
            JsonNode rootNode = objectMapper.readTree(response);
            JsonNode item = rootNode.path("response").path("body").path("item");

            if (item.isMissingNode()) {
                throw new PetServiceException(PetErrorCode.FETCH_FAILED, "API_RESPONSE");
            }

            // API 결과를 PetInfoDto로 변환 후 PetServiceDto 생성
            PetInfoDto infoDto = objectMapper.treeToValue(item, PetInfoDto.class);
            return PetServiceDtoFactory.from(infoDto);
        } catch (IOException e) {
            throw new PetServiceException(PetErrorCode.FETCH_FAILED, "IO_EXCEPTION");
        } catch (PetServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new PetServiceException(PetErrorCode.FETCH_FAILED, "GENERAL_EXCEPTION");
        }
    }

    // 펫 저장
    public void savePet(User user, PetServiceDto dto) {
        if (repository.existsByDogRegNo(dto.dogRegNo())) {
            throw new PetServiceException(PetErrorCode.DOG_REG_NO_DUPLICATE);
        }


        //사용자의 이미지가 기본 이미지면 default imgURL 반환, 새 이미지면 Upload 및 새 imgURL 반환
        String imgURL = awsClient.uploadImg(dto.profileImg(), user.getNickname(), (user.getNickname()+"_profileImg"),"강아지_프로필_폴더");


        // DTO에서 Pet 엔티티 생성
        Pet pet = Pet.builder()
                .user(user)
                .dogRegNo(dto.dogRegNo())
                .petName(dto.petName())
                .breed(dto.breed())
                .gender(Gender.fromString(dto.gender()))
                .neuterYn(dto.neuterYn())
                .size(Size.fromString(dto.size()))
                .age(dto.age())
                .temperament(dto.temperament())
                .preferredWalkingLocation(dto.preferredWalkingLocation())
                .profileImg(imgURL)
                .comment(dto.comment())
                .build();

        repository.save(pet);
    }

    // 펫 업데이트
    public void updatePet(Long petId, User user, PetServiceDto updatedDto) {
        Pet existingPet = repository.findByIdAndUser(petId, user)
                .orElseThrow(() -> new PetServiceException(PetErrorCode.PET_NOT_FOUND));

        // 현재 사용자가 리소스 소유자인지 검증
        if (!existingPet.getUser().equals(user)) {
            throw new SecurityException("권한이 없습니다.");
        }

        try {
            // 병합된 DTO를 기반으로 엔티티 생성
            Pet updatedPet = Pet.builder()
                    .id(existingPet.getId())
                    .user(user)
                    .dogRegNo(updatedDto.dogRegNo() != null ? updatedDto.dogRegNo() : existingPet.getDogRegNo())
                    .petName(updatedDto.petName() != null ? updatedDto.petName() : existingPet.getPetName())
                    .breed(updatedDto.breed() != null ? updatedDto.breed() : existingPet.getBreed())
                    .gender(updatedDto.gender() != null ? Gender.fromString(updatedDto.gender()) : existingPet.getGender())
                    .neuterYn(updatedDto.neuterYn() != null ? updatedDto.neuterYn() : existingPet.getNeuterYn())
                    .size(updatedDto.size() != null ? Size.fromString(updatedDto.size()) : existingPet.getSize())
                    .age(updatedDto.age() != null ? updatedDto.age() : existingPet.getAge())
                    .temperament(updatedDto.temperament() != null ? updatedDto.temperament() : existingPet.getTemperament())
                    .preferredWalkingLocation(updatedDto.preferredWalkingLocation() != null ? updatedDto.preferredWalkingLocation() : existingPet.getPreferredWalkingLocation())
                    .profileImg(updatedDto.profileImg() != null ? updatedDto.profileImg() : existingPet.getProfileImg())
                    .comment(updatedDto.comment() != null ? updatedDto.comment() : existingPet.getComment())
                    .createdAt(existingPet.getCreatedAt())
                    .build();

            repository.save(updatedPet);
        } catch (Exception e) {
            throw new PetServiceException(PetErrorCode.UPDATE_FAILED, "GENERAL_EXCEPTION");
        }
    }


    // 펫 삭제
    public void deletePet(Long petId, User user) {
        Pet pet = repository.findByIdAndUser(petId, user)
                .orElseThrow(() -> new PetServiceException(PetErrorCode.PET_NOT_FOUND));
        repository.delete(pet);
    }

    // 이미지 업로드
    public Map<String, String> uploadImage(MultipartFile file) {
        if (file.isEmpty()) {
            throw new ImageServiceException(ImageErrorCode.INVALID_FILE_FORMAT);
        }

        try {
            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            String filePath = uploadDir + fileName;

            File uploadDirFile = new File(uploadDir);
            if (!uploadDirFile.exists() && !uploadDirFile.mkdirs()) {
                throw new ImageServiceException(ImageErrorCode.FILE_UPLOAD_ERROR, "FILE_SYSTEM");
            }

            file.transferTo(new File(filePath));

            Map<String, String> response = new HashMap<>();
            response.put("fileName", fileName);
            response.put("filePath", filePath);
            return response;

        } catch (IOException e) {
            throw new ImageServiceException(ImageErrorCode.FILE_UPLOAD_ERROR, "IO_OPERATION");
        }
    }

    // 외부 API 호출
    private String callApi(String dogRegNo, String ownerNm) throws Exception {
        String baseUrl = "http://apis.data.go.kr/1543061/animalInfoSrvc/animalInfo";
        String serviceKey = "YOUR_SERVICE_KEY";

        StringBuilder urlBuilder = new StringBuilder(baseUrl);
        urlBuilder.append("?").append(URLEncoder.encode("serviceKey", "UTF-8")).append("=").append(serviceKey);
        urlBuilder.append("&").append(URLEncoder.encode("dog_reg_no", "UTF-8")).append("=").append(URLEncoder.encode(dogRegNo, "UTF-8"));
        urlBuilder.append("&").append(URLEncoder.encode("owner_nm", "UTF-8")).append("=").append(URLEncoder.encode(ownerNm, "UTF-8"));
        urlBuilder.append("&").append(URLEncoder.encode("_type", "UTF-8")).append("=").append("json");

        HttpURLConnection conn = (HttpURLConnection) new URL(urlBuilder.toString()).openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/json");

        try (BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = rd.readLine()) != null) {
                response.append(line);
            }
            return response.toString();
        }
    }
}





