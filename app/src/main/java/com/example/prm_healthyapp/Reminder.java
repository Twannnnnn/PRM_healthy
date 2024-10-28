package com.example.prm_healthyapp;

public class Reminder {
    private int id;
    private int userId;
    private int activityId;
    private String reminderTime;
    private boolean isActive;

    public Reminder(int id, int userId, int activityId, String reminderTime, boolean isActive) {
        this.id = id;
        this.userId = userId;
        this.activityId = activityId;
        this.reminderTime = reminderTime;
        this.isActive = isActive;
    }

    public int getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }

    public int getActivityId() {
        return activityId;
    }

    public String getReminderTime() {
        return reminderTime;
    }

    public boolean isActive() {
        return isActive;
    }
}