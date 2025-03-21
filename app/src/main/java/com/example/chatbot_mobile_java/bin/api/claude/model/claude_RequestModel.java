package com.example.chatbot_mobile_java.bin.api.claude.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class claude_RequestModel {
    @SerializedName("model")
    private String model;

    @SerializedName("max_tokens")
    private int maxTokens;

    @SerializedName("system")
    private String system;

    @SerializedName("messages")
    private List<Message> messages;

    public claude_RequestModel(String model, int maxTokens, String system, List<Message> messages) {
        this.model = model;
        this.maxTokens = maxTokens;
        this.system = system;
        this.messages = messages;
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
    }
}
