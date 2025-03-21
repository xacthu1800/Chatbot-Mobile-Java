package com.example.chatbot_mobile_java.bin.api.gpt.remote;

import com.example.chatbot_mobile_java.bin.api.gpt.model.gpt_RequestModel;
import com.example.chatbot_mobile_java.bin.api.gpt.model.gpt_ResponseModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface gpt_ApiClient {
    static final String API_KEY = "8d9ca7804c6b4e2787f7af62e54e130a";

    @Headers({
            "Content-Type: application/json",
            "Authorization: Bearer " + API_KEY
    })
    @POST("v1/chat/completions")
    Call<gpt_ResponseModel> sendRequest(
            @Body gpt_RequestModel gptRequestModel
    );
}
