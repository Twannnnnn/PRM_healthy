package com.example.prm_healthyapp;

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
import java.util.List;
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
                    formatNutritionalInfo(nutritionData);

                    // Save information to the nutrition log
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

    private void formatNutritionalInfo(Map<String, Object> nutritionData) {
        StringBuilder formattedInfo = new StringBuilder();
        formattedInfo.append("Nutritional Info:\n");

        if (nutritionData.containsKey("foods")) {
            List<Map<String, Object>> foods = (List<Map<String, Object>>) nutritionData.get("foods");

            if (foods.isEmpty()) {
                formattedInfo.append("No nutritional data found for the specified food.\n");
            } else {
                for (Map<String, Object> food : foods) {
                    formattedInfo.append("Food Name: ").append(food.get("food_name")).append("\n");
                    formattedInfo.append("Quantity: ").append(food.get("serving_qty")).append("\n");
                    formattedInfo.append("Mass: ").append(food.get("serving_qty"));
                    formattedInfo.append(" ").append(food.get("serving_unit")).append("\n");
                    formattedInfo.append("Calories: ").append(food.get("nf_calories")).append(" kcal\n");
                    formattedInfo.append("Protein: ").append(food.get("nf_protein")).append(" g\n");
                    formattedInfo.append("Fat: ").append(food.get("nf_total_fat")).append(" g\n");
                    formattedInfo.append("Sodium: ").append(food.get("nf_sodium")).append(" g\n");
                    formattedInfo.append("Cholesterol: ").append(food.get("nf_cholesterol")).append(" g\n");
                    formattedInfo.append("Potassium: ").append(food.get("nf_potassium")).append(" g\n");
                    formattedInfo.append("Sugars: ").append(food.get("nf_sugars")).append(" g\n");
                    formattedInfo.append("Saturated Fat: ").append(food.get("nf_saturated_fat")).append(" g\n");
                    formattedInfo.append("Carbohydrates: ").append(food.get("nf_total_carbohydrate")).append(" g\n");
                    formattedInfo.append("\n"); // Add a line break between different foods
                }
            }
        } else if (nutritionData.containsKey("message")) {
            // Handle specific error messages returned from the API
            String errorMessage = (String) nutritionData.get("message");
            formattedInfo.append("Error: ").append(errorMessage).append("\n");
        } else {
            formattedInfo.append("No nutritional data found.\n");
        }

        textViewResult.setText(formattedInfo.toString());
    }

    private void saveNutritionLog(Map<String, Object> nutritionData) {
        SQLiteDatabase db = new DatabaseHelper(this).getWritableDatabase();
        ContentValues values = new ContentValues();

        // Assuming you have extracted all necessary information from nutritionData
        values.put("food_name", (String) nutritionData.get("food_name")); // Adjust key as needed
        values.put("calories", (Float) nutritionData.get("calories")); // Adjust key as needed
        values.put("fat", (Float) nutritionData.get("fat")); // Adjust key as needed
        values.put("protein", (Float) nutritionData.get("protein")); // Adjust key as needed
        values.put("carbohydrates", (Float) nutritionData.get("carbohydrates")); // Adjust key as needed
        values.put("fiber", (Float) nutritionData.get("fiber")); // Adjust key as needed
        values.put("vitamins", (String) nutritionData.get("vitamins")); // Adjust key as needed
        values.put("minerals", (String) nutritionData.get("minerals")); // Adjust key as needed

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