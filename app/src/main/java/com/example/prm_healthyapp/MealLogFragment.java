package com.example.prm_healthyapp;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;

import java.util.ArrayList;
import java.util.Calendar;
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

        // Set up the Nutrition Total button
        Button btnNutritionTotal = view.findViewById(R.id.btnNutritionTotal);
        btnNutritionTotal.setOnClickListener(v -> showDateRangePicker());

        return view;
    }

    private void loadMealLogs(int userId) {
        Cursor cursor = dbHelper.getAllMealLogs(userId);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                try {
                    String logTime = cursor.getString(cursor.getColumnIndexOrThrow("meal_time"));
                    String logDate = cursor.getString(cursor.getColumnIndexOrThrow("log_date")); // Retrieve log date
                    String title = cursor.getString(cursor.getColumnIndexOrThrow("meal_type"));
                    String description = cursor.getString(cursor.getColumnIndexOrThrow("food_items"));
                    String foodMass = cursor.getString(cursor.getColumnIndexOrThrow("food_mass"));

                    // Create LogItem with all required parameters
                    LogItem logItem = new LogItem("Meal", logTime, logDate, title, description + " (" + foodMass + ")", foodMass);
                    logList.add(logItem);
                } catch (IllegalArgumentException e) {
                    Log.e("MealLogFragment", "Column not found: " + e.getMessage());
                }
            } while (cursor.moveToNext());
            cursor.close();
        }
    }
    private void loadSleepLogs(int userId) {
        Cursor cursor = dbHelper.getAllSleepLogs(userId);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                String sleepStart = cursor.getString(cursor.getColumnIndexOrThrow("sleep_start"));
                String sleepEnd = cursor.getString(cursor.getColumnIndexOrThrow("sleep_end"));
                String logDate = cursor.getString(cursor.getColumnIndexOrThrow("log_date")); // Retrieve log date
                float duration = cursor.getFloat(cursor.getColumnIndexOrThrow("duration"));

                LogItem logItem = new LogItem("Sleep", sleepStart, logDate, "Sleep Log", sleepEnd, ""); // Food mass can be empty for sleep logs
                logList.add(logItem);
            } while (cursor.moveToNext());
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
        String foodMass = logItem.getFoodMass(); // Replace with actual mass if available

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

    private void showDateRangePicker() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_date_range, null);
        builder.setView(dialogView);

        EditText etStartDate = dialogView.findViewById(R.id.etStartDate);
        EditText etEndDate = dialogView.findViewById(R.id.etEndDate);
        Button btnCalculate = dialogView.findViewById(R.id.btnCalculate);
        Button btnClose = dialogView.findViewById(R.id.btnClose);

        AlertDialog dialog = builder.create();

        etStartDate.setOnClickListener(v -> showDatePicker(etStartDate));
        etEndDate.setOnClickListener(v -> showDatePicker(etEndDate));

        btnCalculate.setOnClickListener(v -> {
            String startDate = etStartDate.getText().toString();
            String endDate = etEndDate.getText().toString();
            if (!startDate.isEmpty() && !endDate.isEmpty()) {
                calculateNutritionTotal(startDate, endDate);
                dialog.dismiss();
            } else {
                Toast.makeText(getContext(), "Please select both dates", Toast.LENGTH_SHORT).show();
            }
        });

        btnClose.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }

    private void showDatePicker(EditText editText) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), (view, selectedYear, selectedMonth, selectedDay) -> {
            String date = selectedYear + "-" + (selectedMonth + 1) + "-" + selectedDay;
            editText.setText(date);
        }, year, month, day);
        datePickerDialog.show();
    }
    @SuppressLint("Range")
    private void calculateNutritionTotal(String startDate, String endDate) {
        // Set default date range to one day if no arguments are provided
        if (startDate.isEmpty() || endDate.isEmpty()) {
            Calendar calendar = Calendar.getInstance();
            endDate = String.format("%d-%02d-%02d", calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH) + 1,
                    calendar.get(Calendar.DAY_OF_MONTH));
            calendar.add(Calendar.DAY_OF_MONTH, -1); // Subtract one day for start date
            startDate = String.format("%d-%02d-%02d", calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH) + 1,
                    calendar.get(Calendar.DAY_OF_MONTH));
        }

        Cursor cursor = dbHelper.getAllMealLogs(1); // Replace with actual userId

        float totalCalories = 0;
        float totalProtein = 0;
        float totalFat = 0;
        float totalCarbohydrates = 0;

        while (cursor.moveToNext()) {
            String mealLogDate = cursor.getString(cursor.getColumnIndex("log_date"));
            if (isDateInRange(mealLogDate, startDate, endDate)) {
                totalCalories += cursor.getFloat(cursor.getColumnIndex("total_calories"));
                // Assuming you have these columns in your meal_log table
                totalProtein += cursor.getFloat(cursor.getColumnIndex("total_protein")); // Add protein column
                totalFat += cursor.getFloat(cursor.getColumnIndex("total_fat")); // Add fat column
                totalCarbohydrates += cursor.getFloat(cursor.getColumnIndex("total_carbohydrates")); // Add carbs column
            }
        }
        cursor.close();

        String resultMessage = String.format("Total Calories: %.2f\nTotal Protein: %.2f g\nTotal Fat: %.2f g\nTotal Carbohydrates: %.2f g",
                totalCalories, totalProtein, totalFat, totalCarbohydrates);
        showResultDialog(resultMessage);
    }

    private boolean isDateInRange(String logDate, String startDate, String endDate) {
        return logDate.compareTo(startDate) >= 0 && logDate.compareTo(endDate) <= 0;
    }

    private void showResultDialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Nutrition Total")
                .setMessage(message)
                .setPositiveButton("OK", null)
                .show();
    }
}