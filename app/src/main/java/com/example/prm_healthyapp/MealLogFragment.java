package com.example.prm_healthyapp;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
    private int completedRequests = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_meal_log, container, false);

        dbHelper = new DatabaseHelper(getActivity());
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        dbHelper = new DatabaseHelper(getActivity());
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
    // Tạo một lớp để lưu trữ các giá trị tổng
    class NutritionTotals {
        float totalCalories = 0;
        float totalProtein = 0;
        float totalFat = 0;
        float totalCarbohydrates = 0;
    }
    @SuppressLint("Range")


// Trong phương thức calculateNutritionTotal
    private void calculateNutritionTotal(String startDate, String endDate) {
        Cursor cursor = dbHelper.getAllMealLogs(1);  // Sử dụng userID thực tế

        NutritionTotals totals = new NutritionTotals();
        List<String> foodItemsList = new ArrayList<>();

        while (cursor.moveToNext()) {
            String mealLogDate = cursor.getString(cursor.getColumnIndex("log_date"));
            if (isDateInRange(mealLogDate, startDate, endDate)) {
                String foodItems = cursor.getString(cursor.getColumnIndex("food_items"));
                foodItemsList.add(foodItems);
            }
        }
        cursor.close();

        completedRequests = 0; // Khởi tạo biến đếm

        for (String foodItems : foodItemsList) {
            fetchNutritionalInfoFromApi(foodItems, (calories, protein, fat, carbohydrates) -> {
                synchronized (this) {  // Đồng bộ hóa để tránh xung đột
                    totals.totalCalories += calories;
                    totals.totalProtein += protein;
                    totals.totalFat += fat;
                    totals.totalCarbohydrates += carbohydrates;

                    completedRequests++;  // Tăng số lượng yêu cầu đã hoàn thành

                    if (completedRequests == foodItemsList.size()) {  // Kiểm tra điều kiện hoàn thành
                        String resultMessage = String.format("Total Calories: %.2f\nTotal Protein: %.2f g\nTotal Fat: %.2f g\nTotal Carbohydrates: %.2f g",
                                totals.totalCalories, totals.totalProtein, totals.totalFat, totals.totalCarbohydrates);
                        showResultDialog(resultMessage);  // Hiển thị kết quả
                    }
                }
            });
        }
    }
    private void fetchNutritionalInfoFromApi(String foodItems, NutritionalDataCallback callback) {
        // Khởi tạo Retrofit và gọi API để lấy thông tin dinh dưỡng
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://trackapi.nutritionix.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        NutritionixApiService nutritionixApiService = retrofit.create(NutritionixApiService.class);

        Map<String, String> options = new HashMap<>();
        options.put("query", foodItems);

        // Thay thế với mã API của bạn
        Call<Map<String, Object>> call = nutritionixApiService.getNutritionalInfo(options);
        call.enqueue(new Callback<Map<String, Object>>() {
            @Override
            public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Map<String, Object>> foods = (List<Map<String, Object>>) response.body().get("foods");
                    for (Map<String, Object> food : foods) {
                        // Sử dụng Double thay vì Float
                        double calories = (double) food.get("nf_calories");
                        double protein = (double) food.get("nf_protein");
                        double fat = (double) food.get("nf_total_fat");
                        double carbohydrates = (double) food.get("nf_total_carbohydrate");

                        // Gọi lại callback với dữ liệu
                        callback.onNutritionalDataReceived((float) calories, (float) protein, (float) fat, (float) carbohydrates);
                    }
                } else {
                    Log.e("Nutrition API", "Failed to retrieve nutritional info");
                }
            }

            @Override
            public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                Log.e("Nutrition API", "Error: " + t.getMessage());
            }
        });
    }

    // Interface callback để nhận dữ liệu dinh dưỡng
    interface NutritionalDataCallback {
        void onNutritionalDataReceived(float calories, float protein, float fat, float carbohydrates);
    }
    private void someMethodWhereYouAddMealLog(int userId, String mealType, String foodItems, float totalCalories, float totalProtein, float totalFat, float totalCarbohydrates) {
        // Gọi phương thức addMealLog từ DatabaseHelper
        dbHelper.addMealLog(userId, mealType, foodItems, totalCalories, totalProtein, totalFat, totalCarbohydrates, "meal_time_placeholder", "log_date_placeholder");
    }
    private boolean isDateInRange(String logDate, String startDate, String endDate) {
        return logDate.compareTo(startDate) >= 0 && logDate.compareTo(endDate) <= 0;
    }
    private String createAdviceRequest(User user) {
        StringBuilder requestBuilder = new StringBuilder();
        requestBuilder.append("User Information:\n")
                .append("Name: ").append(user.getName()).append("\n")
                .append("Age: ").append(user.getAge()).append("\n")
                .append("Weight: ").append(user.getWeight()).append("\n")
                .append("Height: ").append(user.getHeight()).append("\n");

        return requestBuilder.toString();
    }
    private void showResultDialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Nutrition Total")
                .setMessage(message)
                .setPositiveButton("OK", null)
                .setNegativeButton("Get Advice", (dialog, which) -> {
                    User user = dbHelper.getFirstUser(); // Lấy người dùng đầu tiên
                    if (user != null) {
                        String adviceRequest = createAdviceRequest(user); // Gọi phương thức ở đây
                        // Sử dụng Intent để chuyển đến HealthAdviceActivityLlama
                        Intent intent = new Intent(getActivity(), HealthAdviceActivityLlama.class);
                        intent.putExtra("adviceRequest", adviceRequest); // Truyền yêu cầu vào Intent
                        startActivity(intent); // Mở Activity mới
                    } else {
                        Toast.makeText(getContext(), "User not found.", Toast.LENGTH_SHORT).show();
                    }
                })
                .show();
    }
}