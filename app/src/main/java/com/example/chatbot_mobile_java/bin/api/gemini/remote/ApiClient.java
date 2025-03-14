package com.example.chatbot_mobile_java.bin.api.gemini.remote;

import com.example.chatbot_mobile_java.bin.api.gemini.model.RequestModel;
import com.example.chatbot_mobile_java.bin.api.gemini.model.ResponseModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiClient {
    @POST("v1beta/models/gemini-2.0-flash:generateContent")
    Call<ResponseModel> sendRequest(
            @Query("key") String key,
            @Body RequestModel requestModel
    );
}
