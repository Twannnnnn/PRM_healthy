package com.example.prm_healthyapp; // Thay đổi theo package của bạn

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.HashMap;


import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.HashMap;
import java.util.Map;



import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.HashMap;
import java.util.Map;

public class HealthAdviceActivity extends AppCompatActivity {
    private EditText editTextInput;
    private Button buttonAnalyze;
    private TextView textViewResult;
    private NutritionixApiService nutritionixApiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_advice);

        editTextInput = findViewById(R.id.editTextInput);
        buttonAnalyze = findViewById(R.id.buttonPredict);
        textViewResult = findViewById(R.id.textViewResult);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://trackapi.nutritionix.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        nutritionixApiService = retrofit.create(NutritionixApiService.class);

        buttonAnalyze.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String foodDescription = editTextInput.getText().toString();
                analyzeFood(foodDescription);
            }
        });
    }

    private void analyzeFood(String description) {
        Map<String, String> options = new HashMap<>();
        options.put("query", description);

        nutritionixApiService.getNutritionalInfo(options).enqueue(new Callback<Map<String, Object>>() {
            @Override
            public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Map<String, Object> nutritionData = response.body();
                    textViewResult.setText("Nutritional Info:\n" + nutritionData.toString());

                    // Lưu thông tin dinh dưỡng vào nutrition_log
                    saveNutritionLog(nutritionData);
                } else {
                    handleError(response);
                }
            }

            @Override
            public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                Log.e("Error", t.getMessage());
                textViewResult.setText("Failed to retrieve data: " + t.getMessage());
            }
        });
    }

    private void saveNutritionLog(Map<String, Object> nutritionData) {
        SQLiteDatabase db = new DatabaseHelper(this).getWritableDatabase();
        ContentValues values = new ContentValues();

        // Giả sử bạn đã lấy được tất cả các thông tin cần thiết từ nutritionData
        // Đây là ví dụ, bạn cần điều chỉnh theo cấu trúc của nutritionData
        values.put("food_name", (String) nutritionData.get("food_name")); // Chỉnh sửa theo đúng key
        values.put("calories", (Float) nutritionData.get("calories")); // Chỉnh sửa theo đúng key
        values.put("fat", (Float) nutritionData.get("fat")); // Chỉnh sửa theo đúng key
        values.put("protein", (Float) nutritionData.get("protein")); // Chỉnh sửa theo đúng key
        values.put("carbohydrates", (Float) nutritionData.get("carbohydrates")); // Chỉnh sửa theo đúng key
        values.put("fiber", (Float) nutritionData.get("fiber")); // Chỉnh sửa theo đúng key
        values.put("vitamins", (String) nutritionData.get("vitamins")); // Chỉnh sửa theo đúng key
        values.put("minerals", (String) nutritionData.get("minerals")); // Chỉnh sửa theo đúng key

        db.insert("nutrition_log", null, values);
        db.close();
    }
    private void handleError(Response<Map<String, Object>> response) {
        try {
            String errorResponse = response.errorBody().string();
            Log.e("API Error", errorResponse);
            textViewResult.setText("Error: " + response.code() + " - " + errorResponse);
        } catch (Exception e) {
            Log.e("Error", "Cannot read error.", e);
            textViewResult.setText("Error: " + response.code() + " - Cannot read error.");
        }
    }
}
//public class HealthAdviceActivity extends AppCompatActivity {
//    private EditText editTextInput;
//    private Button buttonPredict;
//    private TextView textViewResult;
//
//    private HuggingFaceApiService huggingFaceApiService;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_health_advice);
//
//        editTextInput = findViewById(R.id.editTextInput);
//        buttonPredict = findViewById(R.id.buttonPredict);
//        textViewResult = findViewById(R.id.textViewResult);
//
//        // Khởi tạo Retrofit
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl("https://api-inference.huggingface.co/models/google-bert/bert-base-uncased/") // Đảm bảo có dấu gạch chéo ở cuối
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//
//        huggingFaceApiService = retrofit.create(HuggingFaceApiService.class);
//
//        buttonPredict.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String inputText = editTextInput.getText().toString();
//                makePrediction(inputText);
//            }
//        });
//    }
//
//    private void makePrediction(String text) {
//        // Kiểm tra xem văn bản có chứa token [MASK] không
//        if (!text.contains("[MASK]")) {
//            text = "The answer to the universe is [MASK]."; // Ví dụ về câu có token
//        }
//
//        HashMap<String, String> inputData = new HashMap<>();
//        inputData.put("inputs", text);
//
//        huggingFaceApiService.getPrediction(inputData).enqueue(new Callback<HashMap<String, Object>>() {
//            @Override
//            public void onResponse(Call<HashMap<String, Object>> call, Response<HashMap<String, Object>> response) {
//                if (response.isSuccessful() && response.body() != null) {
//                    // In ra phản hồi để kiểm tra cấu trúc
//                    Log.d("Response", response.body().toString());
//
//                    // Kiểm tra và xử lý phản hồi
//                    if (response.body() instanceof HashMap) {
//                        HashMap<String, Object> predictions = response.body();
//
//                        // Kiểm tra nếu predictions chứa lỗi
//                        if (predictions.containsKey("error")) {
//                            textViewResult.setText("Lỗi: " + predictions.get("error"));
//                        } else {
//                            // Nếu có dữ liệu liên quan đến dự đoán
//                            textViewResult.setText("Kết quả dự đoán:\n" + predictions.toString());
//                        }
//                    } else {
//                        textViewResult.setText("Phản hồi không đúng định dạng.");
//                    }
//                } else {
//                    // Hiển thị thông báo lỗi chi tiết
//                    try {
//                        String errorResponse = response.errorBody().string();
//                        Log.e("Error Response", errorResponse);
//                        textViewResult.setText("Lỗi: " + response.code() + " - " + errorResponse);
//                    } catch (Exception e) {
//                        Log.e("Error", "Không thể đọc lỗi.");
//                        textViewResult.setText("Lỗi: " + response.code() + " - Không thể đọc lỗi.");
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(Call<HashMap<String, Object>> call, Throwable t) {
//                Log.e("Error", t.getMessage());
//                textViewResult.setText("Có lỗi xảy ra: " + t.getMessage());
//            }
//        });
//    }
//}