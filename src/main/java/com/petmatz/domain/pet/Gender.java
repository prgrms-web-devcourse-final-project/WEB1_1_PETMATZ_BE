package com.petmatz.domain.pet;

public enum Gender {
    MALE("수컷"),
    FEMALE("암컷");

    private final String koreanValue;

    Gender(String koreanValue) {
        this.koreanValue = koreanValue;
    }

    public static Gender fromString(String gender) {
        return switch (gender) {
            case "수컷" -> MALE;
            case "암컷" -> FEMALE;
            default -> throw new IllegalArgumentException("Invalid gender: " + gender);
        };
    }

    @Override
    public String toString() {
        return name();
    }
}
