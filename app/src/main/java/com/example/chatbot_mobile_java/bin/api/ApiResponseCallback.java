package com.example.chatbot_mobile_java.bin.api;

public interface ApiResponseCallback {
    void onSuccess(String response);
    void onError(String error);
}
