package com.example.chatbot_mobile_java.bin.data;

public class history_chat {
    public String getChatText() {
        return chatText;
    }

    public history_chat(String chatText) {
        this.chatText = chatText;
    }

    public void setChatText(String chatText) {
        this.chatText = chatText;
    }

    private String  chatText ;
}
