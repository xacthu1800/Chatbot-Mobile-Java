package com.example.chatbot_mobile_java.bin.api;

import android.util.Log;

import com.example.chatbot_mobile_java.bin.api.claude.model.claude_RequestModel;
import com.example.chatbot_mobile_java.bin.api.claude.model.claude_ResponseModel;
import com.example.chatbot_mobile_java.bin.api.claude.remote.claude_ApiService;
import com.example.chatbot_mobile_java.bin.api.gpt.model.gpt_RequestModel;
import com.example.chatbot_mobile_java.bin.api.gpt.model.gpt_ResponseModel;
import com.example.chatbot_mobile_java.bin.api.gpt.remote.gpt_ApiService;
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

    public static String resolveApiMessage(clientMessage message, ApiResponseCallback callback) {


        try {
            switch (message.get_Type()) {
                case "Gemini 2.0 Pro Experimental":
                    return handleGeminiApi(message.get_Text(), callback);
                case "OpenAI GPT-4o-mini":
                    return handleGpt(message.get_Text(), callback);
                case "Claude 3.7":
                    return handleClaudeApi(message.get_Text(), callback);
                default:
                    return "Error: Unsupported API type";
            }
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    private static String handleGeminiApi(String text, ApiResponseCallback callback) throws Exception{
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
                    callback.onSuccess(generatedText);

                } else {
                    // Xử lý lỗi
                    callback.onError("Error: " + response.code());
                    Log.e("API_ERROR", "Lỗi: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                // Xử lý khi request thất bại
                callback.onError("Request failed: " + t.getMessage());
                Log.e("API_FAILURE", "Request thất bại: " + t.getMessage());
            }
        });

        return responseText[0];
    }
    private static String handleGpt(String text,ApiResponseCallback callback ) throws Exception {
        gpt_RequestModel.Message userMessage = new gpt_RequestModel.Message("user", text);
        List<gpt_RequestModel.Message> messages = List.of(userMessage);
        gpt_RequestModel requestModel = new gpt_RequestModel("gpt-4o-mini-2024-07-18", messages);

//        call atrofit
        Call<gpt_ResponseModel> call = gpt_ApiService.sendGptRequest(requestModel);
        call.enqueue(new Callback<gpt_ResponseModel>() {
            @Override
            public void onResponse(Call<gpt_ResponseModel> call, Response<gpt_ResponseModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    gpt_ResponseModel result = response.body();
                    // Xử lý kết quả
                    String generatedText = result.getChoices().get(0).getMessage().getContent();
                    callback.onSuccess(generatedText);

                } else {
                    // Xử lý lỗi
                    callback.onError("Error: " + response.code());
                    Log.e("API_ERROR", "Lỗi: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<gpt_ResponseModel> call, Throwable t) {
                // Xử lý khi request thất bại
                callback.onError("Request failed: " + t.getMessage());
                Log.e("API_FAILURE", "Request thất bại: " + t.getMessage());
            }

        });

        return responseText[0];
    }
    private static String handleClaudeApi(String text, ApiResponseCallback callback) throws Exception {
        claude_RequestModel.Message userMessage = new claude_RequestModel.Message("user", text);
        List<claude_RequestModel.Message> messages = List.of(userMessage);
        claude_RequestModel requestModel = new claude_RequestModel("claude-3-7-sonnet-20250219", 2048, "You are an AI assistant who knows everything.",messages);

//        call atrofit
        Call<claude_ResponseModel> call = claude_ApiService.sendClaudeRequest(requestModel);
        call.enqueue(new Callback<claude_ResponseModel>() {
            @Override
            public void onResponse(Call<claude_ResponseModel> call, Response<claude_ResponseModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    claude_ResponseModel result = response.body();
                    // Xử lý kết quả
                    String generatedText = result.getContent().get(0).getText();
                    callback.onSuccess(generatedText);

                } else {
                    // Xử lý lỗi
                    callback.onError("Error: " + response.code());
                    Log.e("API_ERROR", "Lỗi: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<claude_ResponseModel> call, Throwable t) {
                callback.onError("Request failed: " + t.getMessage());
                Log.e("API_FAILURE", "Request thất bại: " + t.getMessage());
            }


        });

        return responseText[0];
    }
}

