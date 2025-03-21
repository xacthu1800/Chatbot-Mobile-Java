package com.example.chatbot_mobile_java.bin.api.gpt.remote;

import com.example.chatbot_mobile_java.bin.api.gpt.model.gpt_RequestModel;
import com.example.chatbot_mobile_java.bin.api.gpt.model.gpt_ResponseModel;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class gpt_ApiService {
    private static final String BASE_URL = "https://api.aimlapi.com/";

    private static Retrofit retrofit;
    private static gpt_ApiClient gptApiClient;

    public static Retrofit getRetrofitInstance(){
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public static gpt_ApiClient getApiClient() {
        if (gptApiClient == null) {
            gptApiClient = getRetrofitInstance().create(gpt_ApiClient.class);
        }
        return gptApiClient;
    }

    public static Call<gpt_ResponseModel> sendGptRequest(gpt_RequestModel gptRequestModel) {
        return getApiClient().sendRequest(gptRequestModel);
    }
}
