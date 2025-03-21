package com.example.chatbot_mobile_java.bin.api.claude.remote;

import com.example.chatbot_mobile_java.bin.api.claude.model.claude_RequestModel;
import com.example.chatbot_mobile_java.bin.api.claude.model.claude_ResponseModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface claude_ApiClient {
    static final String API_KEY = "8d9ca7804c6b4e2787f7af62e54e130a";

    @Headers({
            "Content-Type: application/json",
            "Authorization: Bearer " + API_KEY
    })
    @POST("v1/messages")
    Call<claude_ResponseModel> sendRequest(
            @Body claude_RequestModel gptRequestModel
    );
}
