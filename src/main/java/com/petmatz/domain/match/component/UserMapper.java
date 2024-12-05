package com.petmatz.domain.match.component;

import com.petmatz.domain.match.dto.response.DetailedMatchResultResponse;
import com.petmatz.domain.match.dto.response.MatchScoreResponse;
import com.petmatz.domain.match.dto.response.UserResponse;
import com.petmatz.domain.match.exception.MatchException;
import com.petmatz.domain.match.utils.MatchUtil;
import com.petmatz.domain.user.entity.User;
import com.petmatz.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

import static com.petmatz.domain.sosboard.exception.SosBoardErrorCode.USER_NOT_FOUND;

@Component
@RequiredArgsConstructor
public class UserMapper {

    private final MatchUtil matchUtil;
    private final UserRepository userRepository;

    public UserResponse mapToUserResponse(Object[] row) {
        long id = ((Number) row[0]).longValue();
        double latitude = ((Number) row[1]).doubleValue();
        double longitude = ((Number) row[2]).doubleValue();
        boolean isCareAvailable = (Boolean) row[3];
        String preferredSize = (String) row[4];
        List<String> preferredSizes = matchUtil.changeList(preferredSize);
        String mbti = (String) row[5];
        double distance = (double) row[6];

        return new UserResponse(id, latitude, longitude, isCareAvailable, preferredSizes, mbti, distance);
    }

    public List<DetailedMatchResultResponse> mapToDetailedMatchResults(List<MatchScoreResponse> matchScores) {
        return matchScores.stream()
                .map(score -> {
                    User user = userRepository.findById(score.id())
                            .orElseThrow(() -> new MatchException(USER_NOT_FOUND, "해당 ID의 사용자를 찾을 수 없습니다: " + score.id()));
                    return new DetailedMatchResultResponse(
                            user.getId(),
                            user.getNickname(),
                            user.getProfileImg(),
                            user.getRecommendationCount(),
                            user.getRegion(),
                            user.getCareCompletionCount(),
                            user.getAccountId()
                    );
                })
                .collect(Collectors.toList());
    }
}
