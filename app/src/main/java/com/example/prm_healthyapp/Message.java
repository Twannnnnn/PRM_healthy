package com.example.prm_healthyapp;

public class Message {
    private String role;
    private String content;

    // Constructor
    public Message(String role, String content) {
        this.role = role;
        this.content = content;
    }
    @Override
    public String toString() {
        return "Message{" +
                "role='" + role + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
    // Getter và Setter
    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}