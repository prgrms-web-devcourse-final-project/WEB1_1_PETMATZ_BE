package com.petmatz.domain.pet;

public enum Size {
    SMALL, MEDIUM, LARGE;

    public static Size fromString(String size) {
        return switch (size.toUpperCase()) {
            case "SMALL" -> SMALL;
            case "MEDIUM" -> MEDIUM;
            case "LARGE" -> LARGE;
            default -> throw new IllegalArgumentException("Invalid size: " + size);
        };
    }
}