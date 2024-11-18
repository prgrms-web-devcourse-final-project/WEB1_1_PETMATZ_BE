package com.petmetz.common.exception;

public record ErrorReason(Integer status, String errorCode, String message) {
    public static ErrorReason of(Integer status, String errorCode, String message) {
        return new ErrorReason(status, errorCode, message);
    }
}
