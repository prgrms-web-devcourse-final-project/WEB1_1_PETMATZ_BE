package com.petmetz.common.config;

import lombok.Getter;

@Getter
public class TraceStatus {

    private TraceId traceId;

    private Long startTimeMs;

    private String message;

    public TraceStatus(TraceId traceId, Long startTimeMs, String message) {
        this.traceId = traceId;
        this.startTimeMs = startTimeMs;
        this.message = message;
    }
}
