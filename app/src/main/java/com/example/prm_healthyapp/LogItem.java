package com.example.prm_healthyapp;

public class LogItem {
    private final String logType;
    private final String logTime;
    private final String title;
    private final String description;

    public LogItem(String logType, String logTime, String title, String description) {
        this.logType = logType;
        this.logTime = logTime;
        this.title = title;
        this.description = description;
    }

    public String getLogType() {
        return logType;
    }

    public String getLogTime() {
        return logTime;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }
}