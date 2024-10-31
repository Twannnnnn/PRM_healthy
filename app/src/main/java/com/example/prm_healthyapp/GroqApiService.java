package com.example.prm_healthyapp;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

import java.util.List;

public interface GroqApiService {

    @GET("openai/v1/models")
    Call<List<Model>> listModels();

    @POST("openai/v1/chat/completions")
    Call<YourResponseType> createChatCompletion(@Body YourRequestType request);

    @GET("openai/v1/models/{modelId}")
    Call<Model> getModel(@Path("modelId") String modelId);
}