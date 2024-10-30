package com.example.prm_healthyapp;

import java.util.Map;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;

public interface NutritionixApiService {
    @Headers({
            "x-app-id: bbff75ba",
            "x-app-key: 01e52221e13a5e5dc3a4b6a892e159be",
            "Content-Type: application/json"
    })
    @POST("v2/natural/nutrients")
    Call<Map<String, Object>> getNutritionalInfo(@Body Map<String, String> options);
}