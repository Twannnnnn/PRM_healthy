package com.example.prm_healthyapp;

public class ActivityModel {
    private int id;
    private int userId;
    private String name;
    private String description;
    private String startTime;
    private String endTime;
    private boolean reminder;

    public ActivityModel(int id, int userId, String name, String description, String startTime, String endTime, boolean reminder) {
        this.id = id;
        this.userId = userId;
        this.name = name;
        this.description = description;
        this.startTime = startTime;
        this.endTime = endTime;
        this.reminder = reminder;
    }

    public int getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public boolean isReminder() {
        return reminder;
    }
}