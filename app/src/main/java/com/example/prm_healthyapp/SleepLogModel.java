package com.example.prm_healthyapp;

public class SleepLogModel {
    private int id;
    private int userId;
    private String sleepStart;
    private String sleepEnd;
    private float duration;
    private String logDate;

    public SleepLogModel(int id, int userId, String sleepStart, String sleepEnd, float duration, String logDate) {
        this.id = id;
        this.userId = userId;
        this.sleepStart = sleepStart;
        this.sleepEnd = sleepEnd;
        this.duration = duration;
        this.logDate = logDate;
    }

    public int getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }

    public String getSleepStart() {
        return sleepStart;
    }

    public String getSleepEnd() {
        return sleepEnd;
    }

    public float getDuration() {
        return duration;
    }

    public String getLogDate() {
        return logDate;
    }
}