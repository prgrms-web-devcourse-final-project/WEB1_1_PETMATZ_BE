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

    /**
     * 좌표를 통해 Kakao API에서 지역 정보를 가져옵니다.
     *
     * @param latitude  위도
     * @param longitude 경도
     * @return KakaoRegion 객체 (지역명 및 행정코드 포함)
     */
    public KakaoRegion getRegionFromCoordinates(double latitude, double longitude) {
        RestTemplate restTemplate = new RestTemplate();
        String url = KAKAO_API_URL + "?x=" + longitude + "&y=" + latitude;

        try {
            // Authorization 헤더 설정
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "KakaoAK " + KAKAO_API_KEY);

            HttpEntity<String> entity = new HttpEntity<>(headers);

            // Kakao API 호출
            ResponseEntity<KakaoGeocodingResponse> response = restTemplate.exchange(
                    url, HttpMethod.GET, entity, KakaoGeocodingResponse.class
            );

            // 응답 상태 코드 확인
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                List<KakaoRegion> regions = response.getBody().getDocuments();

                // 지역 정보가 존재하는지 확인
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

    /**
     * KakaoRegion 정보를 로그에 출력합니다.
     *
     * @param region KakaoRegion 객체
     */
    private void logInfo(KakaoRegion region) {
        System.out.println("Region Name: " + region.getRegionName());
        System.out.println("Region Code: " + region.getCode());
    }

    /**
     * Kakao API 응답을 매핑하기 위한 클래스
     */
    @Data
    static class KakaoGeocodingResponse {
        private List<KakaoRegion> documents;
    }

    /**
     * 지역 정보를 담는 클래스
     */
    @Data
    static class KakaoRegion {
        @JsonProperty("region_1depth_name")
        private String region1; // 예: 서울
        @JsonProperty("region_2depth_name")
        private String region2; // 예: 용인시 처인구
        @JsonProperty("region_3depth_name")
        private String region3; // 예: 역삼동
        @JsonProperty("code")
        private String code;    // 행정구역 코드 (String 타입)

        /**
         * 지역명을 조합하여 반환합니다.
         *
         * @return "서울 강남구"와 같은 형식
         */
        public String getRegionName() {
            return region1 + " " + region2;
        }

        /**
         * 행정코드를 6자리 Integer로 변환합니다.
         *
         * @return 6자리 행정코드(Integer)
         */
        public Integer getCodeAsInteger() {
            try {
                // 6자리로 자르고 Integer로 변환
                String trimmedCode = code.length() > 6 ? code.substring(0, 6) : code;
                return Integer.parseInt(trimmedCode);
            } catch (NumberFormatException e) {
                System.err.println("Failed to parse region code: " + code);
                return null;
            }
        }
    }
}