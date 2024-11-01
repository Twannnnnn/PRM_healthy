package com.example.prm_healthyapp;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AddAfternoonActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private FoodAdapter foodAdapter;
    private DatabaseHelper db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_afternoon);

        // Khởi tạo RecyclerView
        recyclerView = findViewById(R.id.recycler_view_food);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Lấy danh sách thực phẩm từ cơ sở dữ liệu
        DatabaseHelper db = new DatabaseHelper(this);
        List<FoodItem> foodList = db.getAllFoodItems();


        // Khởi tạo FoodAdapter và thiết lập cho RecyclerView
        foodAdapter = new FoodAdapter(foodList);
        recyclerView.setAdapter(foodAdapter);

        // Thiết lập window insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.addafternoon), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Khởi tạo nút thêm bữa sáng và thêm sự kiện click
        ImageView btnAddAfternoon = findViewById(R.id.addButton);
        btnAddAfternoon.setOnClickListener(v -> {
            Intent intent = new Intent(AddAfternoonActivity.this, AddFoodActivity.class);
            startActivity(intent);
        });
        // Confirm button to log the meal
        findViewById(R.id.confirmButton).setOnClickListener(v -> logMeal(foodList));
    }
    private void logMeal(List<FoodItem> foodList) {
        // Get current date and time
        String currentDateTime = java.text.DateFormat.getDateTimeInstance().format(new Date());
        List<FoodItem> selectedFoodItems = foodAdapter.getItemsWithQuantityGreaterThanOne();

        // Convert foodList to a comma-separated string for storing in the database
        StringBuilder foodItems = new StringBuilder();
        StringBuilder foodMass = new StringBuilder();

        double totalCalories = 0.0;

        // Calculate total calories for items with quantity > 1
        for (FoodItem food : selectedFoodItems) {
            foodItems.append(food.getName())
                    .append(" (x").append(food.getQuantity()).append("), ");
            foodMass.append(food.getQuantity() * 50).append("g, "); // Tính food_mass
            totalCalories += food.getTotal_calories() * food.getQuantity();
        }

        // Remove last comma and space
        if (foodItems.length() > 0) {
            foodItems.setLength(foodItems.length() - 2);
        }
        if (foodMass.length() > 0) {
            foodMass.setLength(foodMass.length() - 2);
        }

        DatabaseHelper db = new DatabaseHelper(this);
        db.insertMealLog(1, "Afternoon", foodItems.toString(),foodMass.toString(), totalCalories, currentDateTime, currentDateTime);

        // Show a success message
        Toast.makeText(this, "Meal added successfully!", Toast.LENGTH_SHORT).show();

        // Redirect to DietaryHabitActivity
        Intent intent = new Intent(this, DietaryHabit.class); // DietaryHabitActivity is the Java file linked to activity_dietary_habit.xml
        startActivity(intent);

        // Optional: Finish the current activity if you want to remove it from the back stack
        finish();
    }

}