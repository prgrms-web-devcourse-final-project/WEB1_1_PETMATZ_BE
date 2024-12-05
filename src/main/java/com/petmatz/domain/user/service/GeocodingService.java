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

    public KakaoRegion getRegionFromCoordinates(double latitude, double longitude) {
        RestTemplate restTemplate = new RestTemplate();
        String url = KAKAO_API_URL + "?x=" + longitude + "&y=" + latitude;

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "KakaoAK " + KAKAO_API_KEY);

            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<KakaoGeocodingResponse> response = restTemplate.exchange(
                    url, HttpMethod.GET, entity, KakaoGeocodingResponse.class
            );

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                List<KakaoRegion> regions = response.getBody().getDocuments();

                if (!regions.isEmpty()) {
                    KakaoRegion region = regions.get(0);
                    logInfo(region);
                    return region;
                } else {
                    System.err.println("No region data found for coordinates: " + latitude + ", " + longitude);
                }
            } else {
                System.err.println("Kakao API returned unexpected status code: " + response.getStatusCode());
            }
        } catch (Exception e) {
            System.err.println("Error while fetching region data: " + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }

    private void logInfo(KakaoRegion region) {
        System.out.println("Region Name: " + region.getRegionName());
        System.out.println("Region Code: " + region.getCode());
    }

    @Data
    static class KakaoGeocodingResponse {
        private List<KakaoRegion> documents;
    }

    @Data
    static class KakaoRegion {
        @JsonProperty("region_1depth_name")
        private String region1; // 예: 서울
        @JsonProperty("region_2depth_name")
        private String region2; // 예: 강남구 역삼동
        @JsonProperty("region_3depth_name")
        private String region3; // 예: 역삼동
        @JsonProperty("code")
        private String code;    // 행정구역 코드 (String 타입)

        /**
         * '구'까지만 반환하는 메서드
         *
         * @return "서울 강남구"와 같은 형식
         */
        public String getRegionName() {
            // '구'까지만 표시되도록 region2를 공백 기준으로 split 후 첫 번째 단어 반환
            String region2Trimmed = region2.contains(" ") ? region2.split(" ")[0] : region2;
            return region1 + " " + region2Trimmed;
        }

        public Integer getCodeAsInteger() {
            try {
                String trimmedCode = code.length() > 6 ? code.substring(0, 6) : code;
                return Integer.parseInt(trimmedCode);
            } catch (NumberFormatException e) {
                System.err.println("Failed to parse region code: " + code);
                return null;
            }
        }
    }
}