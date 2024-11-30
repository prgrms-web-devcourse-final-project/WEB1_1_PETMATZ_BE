package com.petmatz.domain.sosboard;

public enum PaymentType {
    HOURLY, DAILY, NEGOTIABLE;

    public static PaymentType fromString(String value) {
        return switch (value.toUpperCase()) {
            case "HOURLY" -> HOURLY;
            case "DAILY" -> DAILY;
            case "NEGOTIABLE" -> NEGOTIABLE;
            default -> throw new IllegalArgumentException("잘못된 결제 유형: " + value);
        };
    }
}
