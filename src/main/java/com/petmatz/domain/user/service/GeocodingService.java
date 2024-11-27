package com.petmatz.domain.user.service;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class GeocodingService {

    private final String KAKAO_API_URL = "https://dapi.kakao.com/v2/local/geo/coord2regioncode.json";
    @Value("${kakao-api-key}")
    private String KAKAO_API_KEY;

    public String getRegionFromCoordinates(double latitude, double longitude) {
        RestTemplate restTemplate = new RestTemplate();
        String url = KAKAO_API_URL + "?x=" + longitude + "&y=" + latitude;

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "KakaoAK " + KAKAO_API_KEY); // 공백 포함!

            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<KakaoGeocodingResponse> response = restTemplate.exchange(
                    url, HttpMethod.GET, entity, KakaoGeocodingResponse.class
            );

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                List<KakaoRegion> regions = response.getBody().getDocuments();
                if (!regions.isEmpty()) {
                    return regions.get(0).getRegionName(); // 가장 상세한 지역명
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    // Kakao API 응답 객체 매핑
    @Data
    static class KakaoGeocodingResponse {
        private List<KakaoRegion> documents;
    }

    @Data
    static class KakaoRegion {
        @JsonProperty("region_1depth_name")
        private String region1; // 예: 서울
        @JsonProperty("region_2depth_name")
        private String region2; // 예: 강남구
        @JsonProperty("region_3depth_name")
        private String region3; // 예: 역삼동

        public String getRegionName() {
            return region1 + " " + region2;
        }
    }
}