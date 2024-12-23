package com.petmatz.domain.pet;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.petmatz.api.pet.dto.PetInfoDto;
import com.petmatz.domain.aws.AwsClient;
import com.petmatz.domain.global.S3ImgDataInfo;
import com.petmatz.domain.pet.entity.Pet;
import com.petmatz.domain.pet.repository.PetRepository;
import com.petmatz.domain.pet.vo.PetInf;
import com.petmatz.domain.pet.dto.PetServiceDtoFactory;
import com.petmatz.domain.pet.exception.PetErrorCode;
import com.petmatz.domain.pet.exception.PetServiceException;
import com.petmatz.domain.pet.vo.PetUpdateInfo;
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
public class PetService {

    private final AwsClient awsClient;

    private final PetRepository repository;
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();


    //TODO Infra로 이전 해야함.
    // 동물 등록 정보를 외부 API로 가져오는 메서드
    public PetInf fetchPetInfo(String dogRegNo, String ownerNm) {
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
    public S3ImgDataInfo savePet(User user, PetInf petInf) throws MalformedURLException {
        if (repository.existsByDogRegNo(petInf.dogRegNo())) {
            throw new PetServiceException(PetErrorCode.DOG_REG_NO_DUPLICATE);
        }

        //6-1 Img 정제
        URL uploadURL = awsClient.uploadImg(user.getAccountId(), petInf.profileImg(), "PET_IMG", petInf.dogRegNo());
        String imgURL = uploadURL.getProtocol() + "://" + uploadURL.getHost() + uploadURL.getPath();
        String resultImgURL = String.valueOf(uploadURL);
        if (petInf.profileImg().startsWith("profile")) {
            imgURL = uploadURL.getProtocol() + "://" + uploadURL.getHost() + "/기본이미지_폴더/" + petInf.profileImg() + ".svg";
            resultImgURL = "";
        }

        //Pet Entity 생성
        Pet pet = Pet.of(petInf, imgURL, user);

        Pet petEntity = repository.save(pet);
        Long id = petEntity.getId();

        // User의 isRegistered 상태 업데이트
        user.updateUserRegistered();

        return S3ImgDataInfo.of(id, resultImgURL);
    }

    // 펫 업데이트
    public S3ImgDataInfo updatePet(Long petId, User user, PetUpdateInfo petUpdateInfo) throws MalformedURLException {
        Pet existingPet = repository.findByIdAndUser(petId, user)
                .orElseThrow(() -> new PetServiceException(PetErrorCode.PET_NOT_FOUND));

        // 현재 사용자가 리소스 소유자인지 검증
        if (!existingPet.getUser().equals(user)) {
            throw new SecurityException("권한이 없습니다.");
        }

        String imgURL = petUpdateInfo.profileImg();
        String resultImgURL = "";
        if (!existingPet.checkImgURL(petUpdateInfo.profileImg())) {
            //6-1 Img 정제
            URL uploadURL = awsClient.uploadImg(user.getAccountId(), petUpdateInfo.profileImg(), "PET_IMG", existingPet.getDogRegNo());
            imgURL = uploadURL.getProtocol() + "://" + uploadURL.getHost() + uploadURL.getPath();
            resultImgURL = String.valueOf(uploadURL);
            if (petUpdateInfo.profileImg().startsWith("profile")) {
                imgURL = uploadURL.getProtocol() + "://" + uploadURL.getHost() + "/기본이미지_폴더/" + petUpdateInfo.profileImg() + ".svg";
                resultImgURL = "";
            }
        }

        // 병합된 DTO를 기반으로 엔티티 생성
        existingPet.updatePetInfo(petUpdateInfo, imgURL);

        return S3ImgDataInfo.of(petId, resultImgURL);
    }


    //TODO Component로 옮기기
    // 펫 삭제
    public void deletePet(Long petId, User user) {
        repository.findByIdAndUser(petId, user)
                .orElseThrow(() -> new PetServiceException(PET_NOT_FOUND));
        repository.deleteById(petId);
//        repository.delete(pet);
    }


    //TODO Infra로 빼야됨.
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





