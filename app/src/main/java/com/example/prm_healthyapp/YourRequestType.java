package com.example.prm_healthyapp;



import java.util.List;



import java.util.List;

public class YourRequestType {
    private List<Message> messages; // Danh sách các tin nhắn
    private String model; // Mô hình AI mà bạn muốn sử dụng

    // Constructor
    public YourRequestType(List<Message> messages, String model) {
        this.messages = messages;
        this.model = model;
    }

    // Getter và Setter cho messages
    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    // Getter và Setter cho model
    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }
}