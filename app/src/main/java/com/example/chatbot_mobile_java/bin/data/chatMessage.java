package com.example.chatbot_mobile_java.bin.data;

public class chatMessage {
    private String content;
    private boolean isCient;
    private long timestamp;

    public chatMessage(String content, boolean isClient) {
        this.content = content;
        this.isCient  = isClient;
        this.timestamp = System.currentTimeMillis();
    }

    public String getContent() {
        return content;
    }

    public boolean isCient() {
        return isCient;
    }

    public long getTimestamp() {
        return timestamp;
    }
}
