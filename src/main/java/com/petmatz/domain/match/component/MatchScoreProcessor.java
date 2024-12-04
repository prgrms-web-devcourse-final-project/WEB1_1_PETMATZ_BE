package com.petmatz.domain.match.component;

import com.petmatz.domain.match.dto.response.MatchScoreResponse;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * 점수 정렬에 따라 가져오는 메소드
 */
@Component
public class MatchScoreProcessor {

    public List<MatchScoreResponse> sortMatchScores(List<MatchScoreResponse> matchScores) {
        matchScores.sort(Comparator
                .comparingDouble(MatchScoreResponse::totalScore)
                .thenComparingDouble(MatchScoreResponse::distance)
                .reversed()
        );
        return matchScores;
    }

    public List<MatchScoreResponse> paginateMatchScores(List<MatchScoreResponse> matchScores, int page, int size) {
        int start = page * size;
        int end = Math.min(start + size, matchScores.size());
        if (start >= matchScores.size()) {
            return new ArrayList<>();
        }
        return matchScores.subList(start, end);
    }

    public int calculateTotalPages(long totalElements, int size) {
        return (int) Math.ceil((double) totalElements / size);
    }
}
