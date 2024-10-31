package com.example.prm_healthyapp;

public class Model {
    private String id;
    private String object;
    private long created;
    private String owned_by;
    private boolean active;
    private int context_window;

    // Getters và Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }

    public long getCreated() {
        return created;
    }

    public void setCreated(long created) {
        this.created = created;
    }

    public String getOwnedBy() {
        return owned_by;
    }

    public void setOwnedBy(String owned_by) {
        this.owned_by = owned_by;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public int getContextWindow() {
        return context_window;
    }

    public void setContextWindow(int context_window) {
        this.context_window = context_window;
    }
}