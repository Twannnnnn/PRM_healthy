package com.example.prm_healthyapp;

import java.util.List;

public class YourResponseType {
    private String id;
    private String object;
    private List<Choice> choices;

    // Getters và Setters

    public static class Choice {
        private Message message;

        public Message getMessage() {
            return message;
        }
        // Getters và Setters

        @Override
        public String toString() {
            return "Choice{" +
                    "message=" + message.toString() + // Cần ghi đè toString cho class Message
                    '}';
        }
    }
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder("YourResponseType{\n");
        result.append("id='").append(id).append("',\n");
        result.append("object='").append(object).append("',\n");
        result.append("choices=[\n");
        for (Choice choice : choices) {
            result.append(choice.toString()).append(",\n"); // Cần ghi đè toString cho class Choice
        }
        result.append("]\n");
        result.append("}");
        return result.toString();
    }

    // Định nghĩa lớp Choice bên trong hoặc ngoài

    public List<Choice> getChoices() {
        return choices;
    }

    public void setChoices(List<Choice> choices) {
        this.choices = choices;
    }

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
