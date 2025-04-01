package com.petmatz.domain.pet;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.petmatz.api.pet.dto.PetInfoDto;
import com.petmatz.domain.aws.AwsClient;
import com.petmatz.domain.pet.dto.PetSaveResponse;
import com.petmatz.domain.pet.dto.PetServiceDto;
import com.petmatz.domain.pet.dto.PetServiceDtoFactory;
import com.petmatz.domain.pet.dto.PetUpdateResponse;
import com.petmatz.domain.pet.exception.PetErrorCode;
import com.petmatz.domain.pet.exception.PetServiceException;
import com.petmatz.domain.user.entity.User;
import com.petmatz.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;
import java.util.stream.Collectors;

import static com.petmatz.domain.pet.exception.PetErrorCode.PET_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class PetServiceImpl implements PetService{

    private final AwsClient awsClient;

    private final PetRepository repository;
    private final UserRepository userRepository;
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
    public PetSaveResponse savePet(User user, PetServiceDto dto) throws MalformedURLException {
        if (repository.existsByDogRegNo(dto.dogRegNo())) {
            throw new PetServiceException(PetErrorCode.DOG_REG_NO_DUPLICATE);
        }

        URL uploadURL = awsClient.uploadImg(user.getAccountId(), dto.profileImg(), "PET_IMG", dto.dogRegNo());
        String profileImg = uploadURL.getProtocol() + "://" + uploadURL.getHost() + uploadURL.getPath();

        if (dto.profileImg().startsWith("profile")) {
            profileImg = uploadURL.getProtocol() + "://" + uploadURL.getHost() + "/기본이미지_폴더/" + dto.profileImg() + ".svg";
        }

        Pet pet = Pet.createFromDto(user, dto, profileImg);
        Long id = repository.save(pet).getId();

        user.markAsRegistered();
        userRepository.save(user);

        return PetSaveResponse.of(id, profileImg);
    }

    // 펫 업데이트
    public PetUpdateResponse updatePet(Long petId, User user, PetServiceDto updatedDto) throws MalformedURLException {
        Pet existingPet = repository.findByIdAndUser(petId, user)
                .orElseThrow(() -> new PetServiceException(PetErrorCode.PET_NOT_FOUND));

        String profileImg = existingPet.getProfileImg();
        if (!existingPet.checkImgURL(updatedDto.profileImg())) {
            URL uploadURL = awsClient.uploadImg(user.getAccountId(), updatedDto.profileImg(), "PET_IMG", existingPet.getDogRegNo());
            profileImg = uploadURL.getProtocol() + "://" + uploadURL.getHost() + uploadURL.getPath();
            if (updatedDto.profileImg().startsWith("profile")) {
                profileImg = uploadURL.getProtocol() + "://" + uploadURL.getHost() + "/기본이미지_폴더/" + updatedDto.profileImg() + ".svg";
            }
        }

        existingPet.updateFromDto(updatedDto, profileImg);
        repository.save(existingPet);

        return PetUpdateResponse.of(existingPet.getDogRegNo(), profileImg);
    }


    // 펫 삭제
    public void deletePet(Long petId, User user) {
        Pet pet = repository.findByIdAndUser(petId, user)
                .orElseThrow(() -> new PetServiceException(PET_NOT_FOUND));
        repository.delete(pet);
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

    public List<Pet> getPetsByUserId(Long userId) {
        List<Pet> userPets = repository.findByUserId(userId);
        if (userPets.isEmpty()) {
            throw new PetServiceException(PET_NOT_FOUND);
        }
        return userPets;
    }

    public List<String> getTemperamentsByUserId(Long userId) {
        List<Pet> pets = repository.findByUserId(userId);
        if (pets.isEmpty()) {
            throw new RuntimeException();
        }
        return pets.stream()
                .map(pet -> pet.getTemperament() != null ? pet.getTemperament() : "Unknown")
                .collect(Collectors.toList());
    }

}





