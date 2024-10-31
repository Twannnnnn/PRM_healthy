package com.example.prm_healthyapp;

public class LogItem {
    private final String logType;
    private final String logTime;
    private final String logDate; // Field for log date
    private final String title;
    private final String description;
    private final String foodMass;

    // Constructor to initialize all fields
    public LogItem(String logType, String logTime, String logDate, String title, String description, String foodMass) {
        this.logType = logType;
        this.logTime = logTime;
        this.logDate = logDate; // Initialize log date
        this.title = title;
        this.description = description;
        this.foodMass = foodMass;
    }

    // Getters
    public String getLogType() { return logType; }
    public String getLogTime() { return logTime; }
    public String getLogDate() { return logDate; } // Getter for log date
    public String getFoodMass() { return foodMass; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
}