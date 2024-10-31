package com.example.prm_healthyapp;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface LlamaApiService {
    @POST("openai/v1/chat/completions")
    Call<YourResponseType> createChatCompletion(@Body YourRequestType request);
}