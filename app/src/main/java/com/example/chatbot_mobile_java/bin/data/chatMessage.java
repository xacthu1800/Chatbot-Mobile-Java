package com.example.chatbot_mobile_java.bin.data;

public class chatMessage {
    private String content;
    private boolean isCient;
    private int timestamp;

    public chatMessage(String content, boolean isClient) {
        this.content = content;
        this.isCient  = isClient;
        this.timestamp = (int) (System.currentTimeMillis() / 1000);
    }

    public String getContent() {
        return content;
    }

    public boolean isCient() {
        return isCient;
    }

    public int getTimestamp() {
        return timestamp;
    }
}
