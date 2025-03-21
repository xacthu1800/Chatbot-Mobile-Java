package com.example.chatbot_mobile_java.bin.api.gpt.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class gpt_RequestModel {
    @SerializedName("model")
    private String model;

    @SerializedName("messages")
    private List<Message> messages;

    public gpt_RequestModel(String model, List<Message> messages) {
        this.model = model;
        this.messages = messages;
    }

    public String getModel() {
        return model;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public static class Message {
        @SerializedName("role")
        private String role;

        @SerializedName("content")
        private String content;

        public Message(String role, String content) {
            this.role = role;
            this.content = content;
        }

        public String getRole() {
            return role;
        }

        public String getContent() {
            return content;
        }
    }
}
