package com.example.chatbot_mobile_java.bin.api.claude.remote;

import com.example.chatbot_mobile_java.bin.api.claude.model.claude_RequestModel;
import com.example.chatbot_mobile_java.bin.api.claude.model.claude_ResponseModel;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class claude_ApiService {
    private static final String BASE_URL = "https://api.aimlapi.com/";

    private static Retrofit retrofit;
    private static claude_ApiClient gptApiClient;

    public static Retrofit getRetrofitInstance(){
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public static claude_ApiClient getApiClient() {
        if (gptApiClient == null) {
            gptApiClient = getRetrofitInstance().create(claude_ApiClient.class);
        }
        return gptApiClient;
    }

    public static Call<claude_ResponseModel> sendClaudeRequest(claude_RequestModel gptRequestModel) {
        return getApiClient().sendRequest(gptRequestModel);
    }
}
