package com.example.prm_healthyapp;

import java.util.HashMap;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface HuggingFaceApiService {
    @Headers({
            "Authorization: Bearer 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", // Thay YOUR_TOKEN bằng token của bạn
            "Content-Type: application/json"
    })
    @POST("https://api-inference.huggingface.co/models/google-bert/bert-base-uncased/")
    Call<HashMap<String, Object>> getPrediction(@Body HashMap<String, String> input);
}