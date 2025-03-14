package com.example.chatbot_mobile_java.bin.api.gemini.remote;

import com.example.chatbot_mobile_java.bin.api.gemini.model.RequestModel;
import com.example.chatbot_mobile_java.bin.api.gemini.model.ResponseModel;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiService {
    private static final String BASE_URL = "https://generativelanguage.googleapis.com/";
    private static final String API_KEY = "AIzaSyD2jGK3yeZitZa2h-4zRsK2o73K-vEhDq0";

    private static Retrofit retrofit;
    private static ApiClient apiClient;

    public static Retrofit getRetrofitInstance(){
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public static ApiClient getApiClient() {
        if (apiClient == null) {
            apiClient = getRetrofitInstance().create(ApiClient.class);
        }
        return apiClient;
    }

    public static Call<ResponseModel> sendGeminiRequest(RequestModel requestModel) {
        return getApiClient().sendRequest(API_KEY, requestModel);
    }
}
