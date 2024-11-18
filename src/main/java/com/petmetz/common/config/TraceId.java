package com.petmetz.common.config;

import java.util.UUID;


public class TraceId {

    private String id;

    private int level;

    public TraceId() {
        this.id = createdId();
        this.level = 0;
    }

    private TraceId(String id, int level) {
        this.id = id;
        this.level = level;
    }

    private String createdId() {
        //앞의 8자리만 사용
        return UUID.randomUUID().toString().substring(0, 8);
    }

    public TraceId createdNextId() {
        return new TraceId(id, level + 1);
    }

    public TraceId createdPreviousId() {
        return new TraceId(id, level - 1);
    }

    public boolean isFirstLevel() {
        return level == 0;
    }

    public String getId() {
        return id;
    }

    public int getLevel() {
        return level;
    }
}
