package com.petmatz.domain.user.entity;

import com.petmatz.domain.user.constant.PreferredSize;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Converter
public class PreferredSizeConverter implements AttributeConverter<List<PreferredSize>, String> {

    @Override
    public String convertToDatabaseColumn(List<PreferredSize> attribute) {
        if (attribute == null || attribute.isEmpty()) {
            return ""; // 빈 문자열로 저장
        }
        return attribute.stream()
                .map(Enum::name)
                .collect(Collectors.joining(",")); // 쉼표로 구분된 문자열로 변환
    }

    @Override
    public List<PreferredSize> convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isEmpty()) {
            return new ArrayList<>(); // 빈 리스트 반환
        }
        return Arrays.stream(dbData.split(","))
                .map(PreferredSize::valueOf)
                .collect(Collectors.toList()); // 쉼표로 구분된 문자열을 리스트로 변환
    }
}