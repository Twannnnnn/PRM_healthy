package com.example.prm_healthyapp;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MealLogFragment extends Fragment {
    private RecyclerView recyclerView;
    private LogAdapter logAdapter;
    private List<LogItem> logList;
    private DatabaseHelper dbHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_meal_log, container, false);

        dbHelper = new DatabaseHelper(getActivity());
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        logList = new ArrayList<>();
        loadMealLogs(1); // Replace 1 with the actual userId

        logAdapter = new LogAdapter(logList);
        recyclerView.setAdapter(logAdapter);

        // Set item click listener
        logAdapter.setOnItemClickListener(logItem -> showNutritionalInfoDialog(logItem));

        return view;
    }

    private void loadMealLogs(int userId) {
        Cursor cursor = dbHelper.getAllMealLogs(userId);

        // Ensure the cursor is valid
        if (cursor != null && cursor.moveToFirst()) {
            do {
                try {
                    String logTime = cursor.getString(cursor.getColumnIndexOrThrow("meal_time"));
                    String title = cursor.getString(cursor.getColumnIndexOrThrow("meal_type"));
                    String description = cursor.getString(cursor.getColumnIndexOrThrow("food_items"));
                    String foodMass = cursor.getString(cursor.getColumnIndexOrThrow("food_mass"));

                    LogItem logItem = new LogItem("Meal", logTime, title, description + " (" + foodMass + ")");
                    logList.add(logItem);
                } catch (IllegalArgumentException e) {
                    Log.e("MealLogFragment", "Column not found: " + e.getMessage());
                }
            } while (cursor.moveToNext());
        }

        if (cursor != null) {
            cursor.close();
        }
    }

    private void showNutritionalInfoDialog(LogItem logItem) {
        // Inflate the dialog layout
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_meal_log_details, null);
        AlertDialog dialog = new AlertDialog.Builder(getContext())
                .setView(dialogView)
                .create();

        // Get references to views in the dialog
        TextView tvNutritionalInfo = dialogView.findViewById(R.id.tvNutritionalInfo);
        Button btnClose = dialogView.findViewById(R.id.btnClose);

        // Fetch nutritional info using the food items and mass
        fetchNutritionalInfo(logItem, tvNutritionalInfo);

        // Set close button functionality
        btnClose.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }

    private void fetchNutritionalInfo(LogItem logItem, TextView tvNutritionalInfo) {
        String foodItems = logItem.getDescription(); // This contains food items
        String foodMass = "100"; // Replace with actual mass if available

        // Initialize your Retrofit service
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://trackapi.nutritionix.com/") // Ensure base URL is correct
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        NutritionixApiService nutritionixApiService = retrofit.create(NutritionixApiService.class);

        Map<String, String> options = new HashMap<>();
        options.put("query", foodItems + " " + foodMass); // Combine food items and mass

        // Make the API call
        nutritionixApiService.getNutritionalInfo(options).enqueue(new Callback<Map<String, Object>>() {
            @Override
            public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Map<String, Object> nutritionData = response.body();

                    // Extract relevant nutritional information
                    StringBuilder nutritionalInfo = new StringBuilder();
                    nutritionalInfo.append("Nutritional Info:\n");

                    if (nutritionData.containsKey("foods")) {
                        List<Map<String, Object>> foods = (List<Map<String, Object>>) nutritionData.get("foods");
                        for (Map<String, Object> food : foods) {
                            nutritionalInfo.append("Food Name: ").append(food.get("food_name")).append("\n");
                            nutritionalInfo.append("Calories: ").append(food.get("nf_calories")).append("\n");
                            nutritionalInfo.append("Protein: ").append(food.get("nf_protein")).append("g\n");
                            nutritionalInfo.append("Fat: ").append(food.get("nf_total_fat")).append("g\n");
                            nutritionalInfo.append("Carbohydrates: ").append(food.get("nf_total_carbohydrate")).append("g\n");
                            nutritionalInfo.append("\n");
                        }
                    } else {
                        nutritionalInfo.append("No nutritional data found.");
                    }

                    // Set the nutritional data to TextView
                    tvNutritionalInfo.setText(nutritionalInfo.toString());
                } else {
                    Toast.makeText(getContext(), "Failed to retrieve nutritional info", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                // Handle failure
            }
        });
    }
}