package com.example.chatbot_mobile_java.bin.data;

public class sql_chatMessage {
    private int id;
    private int conversationId;
    private String content;
    private boolean isClient;
    private int timestamp;

    public sql_chatMessage(int id, int conversationId, String content, boolean isClient, int timestamp) {
        this.id = id;
        this.conversationId = conversationId;
        this.content = content;
        this.isClient = isClient;
        this.timestamp = timestamp;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getConversationId() {
        return conversationId;
    }

    public void setConversationId(int conversationId) {
        this.conversationId = conversationId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isClient() {
        return isClient;
    }

    public void setClient(boolean client) {
        isClient = client;
    }

    public int getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }
}
