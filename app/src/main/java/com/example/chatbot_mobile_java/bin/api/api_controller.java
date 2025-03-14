package com.example.chatbot_mobile_java.bin.api;

import android.util.Log;
import com.example.chatbot_mobile_java.bin.data.clientMessage;

import com.example.chatbot_mobile_java.bin.api.gemini.model.RequestModel ;
import com.example.chatbot_mobile_java.bin.api.gemini.model.ResponseModel ;
import com.example.chatbot_mobile_java.bin.api.gemini.remote.ApiService ;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class api_controller {
    static final String[] responseText = {""};

    public static String resolveApiMessage(clientMessage message) {


        try {
            switch (message.get_Type()) {
                case "Gemini 2.0 Pro Experimental":
                    return handleGeminiApi(message.get_Text());
                case "OpenAI GPT-4o-mini":
                    return handleGpt(message.get_Text());
                case "xAI Grok-2":
                    return handleGrokApi(message.get_Text());
                default:
                    return "Error: Unsupported API type";
            }
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    private static String handleGeminiApi(String text) throws Exception{
        RequestModel.Part part = new RequestModel.Part(text);
        RequestModel.Content content = new RequestModel.Content(List.of(part));
        RequestModel requestModel = new RequestModel(List.of(content));

//        call atrofit
        Call<ResponseModel> call = ApiService.sendGeminiRequest(requestModel);
        call.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ResponseModel result = response.body();
                    // Xử lý kết quả
                    String generatedText = result.getCandidates().get(0).getContent().getParts().get(0).getText();
                    responseText[0] = generatedText;
                } else {
                    // Xử lý lỗi
                    responseText[0] = "Error occur onResponse func";
                    Log.e("API_ERROR", "Lỗi: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                // Xử lý khi request thất bại
                responseText[0] = "Error occur in OnFailure func";
                Log.e("API_FAILURE", "Request thất bại: " + t.getMessage());
            }
        });

        return responseText[0];
    }
    private static String handleGpt(String text) throws Exception {
        return "GPT API not implemented yet";
    }
    private static String handleGrokApi(String text) throws Exception {
        return "Grok API not implemented yet";
    }
}

