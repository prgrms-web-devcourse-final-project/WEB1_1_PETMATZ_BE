package com.petmatz.domain.pet;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.petmatz.api.pet.PetInfoDto;
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
public class PetService {

    private final PetRepository repository;
    private final ObjectMapper objectMapper = new ObjectMapper();
    // 실행 디렉토리 기준으로 uploads 폴더 설정
    private final String uploadDir = System.getProperty("user.dir") + "/uploads/";

    // 동물등록번호 유효한지 조회만. 디비에 저장은 X
    public PetServiceDto fetchPetInfo(PetServiceDto serviceDto) {
        try {
            if (repository.existsByDogRegNo(serviceDto.dogRegNo())) {
                throw new PetServiceException(PetErrorCode.DOG_REG_NO_DUPLICATE);
            }

            // API 호출 및 데이터 파싱
            String response = callApi(serviceDto.dogRegNo(), serviceDto.ownerNm());
            JsonNode rootNode = objectMapper.readTree(response);
            JsonNode item = rootNode.path("response").path("body").path("item");

            if (item.isMissingNode()) {
                throw new PetServiceException(PetErrorCode.FETCH_FAILED, "API_RESPONSE");
            }

            // 조회된 데이터를 DTO로 변환
            PetInfoDto infoDto = objectMapper.treeToValue(item, PetInfoDto.class);

            // PetServiceDto 변환
            return PetServiceDto.of(infoDto);
        }catch (IOException e) {
            throw new PetServiceException(PetErrorCode.FETCH_FAILED, "IO_EXCEPTION");
        } catch (PetServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new PetServiceException(PetErrorCode.FETCH_FAILED, "GENERAL_EXCEPTION");
        }
    }

    public void savePet(User user, PetServiceDto serviceDto) {
        // Pet 엔티티 생성
        Pet pet = Pet.builder()
                .user(user)
                .dogRegNo(serviceDto.dogRegNo())
                .petName(serviceDto.petName())
                .breed(serviceDto.breed())
                .gender("수컷".equals(serviceDto.gender()) ? Pet.Gender.수컷 : Pet.Gender.암컷)
                .isNeutered(serviceDto.isNeutered())
                .size(Pet.Size.fromString(serviceDto.size()))
                .age(serviceDto.age())
                .temperament(serviceDto.temperament())
                .preferredWalkingLocation(serviceDto.preferredWalkingLocation())
                .profileImg(serviceDto.profileImg())
                .comment(serviceDto.comment())
                .build();

        // 중복된 동물등록번호 검사
        if (repository.existsByDogRegNo(pet.getDogRegNo())) {
            throw new PetServiceException(PetErrorCode.DOG_REG_NO_DUPLICATE);
        }

        // 데이터 저장
        repository.save(pet);
    }


    public void updatePet(Long id, User user, PetServiceDto serviceDto) {
        Pet existingPet = repository.findByIdAndUser(id, user)
                .orElseThrow(() -> new PetServiceException(PetErrorCode.PET_NOT_FOUND));

        Pet updatedPet = Pet.builder()
                .id(existingPet.getId())
                .user(existingPet.getUser())
                .dogRegNo(existingPet.getDogRegNo())
                .petName(serviceDto.petName() != null ? serviceDto.petName() : existingPet.getPetName())
                .breed(serviceDto.breed() != null ? serviceDto.breed() : existingPet.getBreed())
                .gender(serviceDto.gender() != null ? Pet.Gender.fromString(serviceDto.gender()) : existingPet.getGender())
                .isNeutered(serviceDto.isNeutered() != null ? serviceDto.isNeutered() : existingPet.getIsNeutered())
                .size(serviceDto.size() != null ? Pet.Size.fromString(serviceDto.size()) : existingPet.getSize())
                .age(serviceDto.age() != null ? serviceDto.age() : existingPet.getAge())
                .temperament(serviceDto.temperament() != null ? serviceDto.temperament() : existingPet.getTemperament())
                .preferredWalkingLocation(serviceDto.preferredWalkingLocation() != null ? serviceDto.preferredWalkingLocation() : existingPet.getPreferredWalkingLocation())
                .profileImg(serviceDto.profileImg() != null ? serviceDto.profileImg() : existingPet.getProfileImg())
                .comment(serviceDto.comment() != null ? serviceDto.comment() : existingPet.getComment())
                .build();

        repository.save(updatedPet);
    }

    public void deletePet(Long id, User user) {
        Pet pet = repository.findByIdAndUser(id, user)
                .orElseThrow(() -> new PetServiceException(PetErrorCode.PET_NOT_FOUND));
        repository.delete(pet);
    }

    public Map<String, String> uploadImage(MultipartFile file) {
        try {
            // 파일 비어있는지 검증
            if (file.isEmpty()) {
                throw new ImageServiceException(ImageErrorCode.INVALID_FILE_FORMAT);
            }

            // 파일 이름 생성
            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            String filePath = uploadDir + fileName;

            // 디렉토리 생성
            File uploadDirFile = new File(uploadDir);
            if (!uploadDirFile.exists() && !uploadDirFile.mkdirs()) {
                throw new ImageServiceException(ImageErrorCode.FILE_UPLOAD_ERROR, "FILE_SYSTEM");
            }

            // 파일 저장
            file.transferTo(new File(filePath));

            // 파일 정보 반환
            Map<String, String> response = new HashMap<>();
            response.put("fileName", fileName);
            response.put("filePath", filePath);
            return response;

        } catch (IOException e) {
            throw new ImageServiceException(ImageErrorCode.FILE_UPLOAD_ERROR, "IO_OPERATION");
        }
    }

    private String callApi(String dogRegNo, String ownerNm) throws Exception {
        String baseUrl = "http://apis.data.go.kr/1543061/animalInfoSrvc/animalInfo";
        String serviceKey = "pKvmFkk28rLMAzf45F57ROEOkGB1ofd1JjdhjC7yEGjr7YKzJ107n9o1GKJPU5jNobwFuNmZn7%2B3QLMfi%2FP6XQ%3D%3D";

        StringBuilder urlBuilder = new StringBuilder(baseUrl);
        urlBuilder.append("?" + URLEncoder.encode("serviceKey", "UTF-8") + "=" + serviceKey);
        urlBuilder.append("&" + URLEncoder.encode("dog_reg_no", "UTF-8") + "=" + URLEncoder.encode(dogRegNo, "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("owner_nm", "UTF-8") + "=" + URLEncoder.encode(ownerNm, "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("_type", "UTF-8") + "=" + URLEncoder.encode("json", "UTF-8"));

        URL url = new URL(urlBuilder.toString());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/json");

        BufferedReader rd = (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300)
                ? new BufferedReader(new InputStreamReader(conn.getInputStream()))
                : new BufferedReader(new InputStreamReader(conn.getErrorStream()));

        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            sb.append(line);
        }
        rd.close();
        conn.disconnect();

        return sb.toString();
    }
}




