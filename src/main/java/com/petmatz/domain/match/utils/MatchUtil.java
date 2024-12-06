package com.petmatz.domain.match.utils;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class MatchUtil {

    public List<String> changeList(String preferredSizeString) {
        return preferredSizeString == null || preferredSizeString.isEmpty()
                ? new ArrayList<>()
                : Arrays.asList(preferredSizeString.split(","));
    }
}