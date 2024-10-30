package com.example.prm_healthyapp;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface PredictionService {
    @POST("/predict")
    Call<List<List<Float>>> getPrediction(@Body HashMap<String, String> requestBody);
}