package com.example.prm_healthyapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DietaryHabit extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dietary_habit);


        TextView breakfastCaloriesText = findViewById(R.id.breakfastCalories);
        TextView afternoonCaloriesText = findViewById(R.id.afternoonCalories);
        TextView lunchCaloriesText = findViewById(R.id.luchCalories);
        TextView dinnerCaloriesText = findViewById(R.id.dinnerCalories);
        TextView totalCaloriesText = findViewById(R.id.caloriesText);
        TextView dateText = findViewById(R.id.date);

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String currentDate = sdf.format(new Date());

        DatabaseHelper dbHelper = new DatabaseHelper(this);

        double breakfastCalories = dbHelper.getBreakfastCalories(1); // Pass userId = 1
        double afternoonCalories = dbHelper.getAfternoonCalories(1); // Pass userId = 1
        double lunchCalories = dbHelper.getLunchCalories(1); // Pass userId = 1
        double dinnerCalories = dbHelper.getDinnerCalories(1); // Pass userId = 1




        double totalCalo = breakfastCalories + afternoonCalories + lunchCalories + dinnerCalories;

        breakfastCaloriesText.setText("Calories: " + breakfastCalories + " kcal");
        afternoonCaloriesText.setText("Calories: " + afternoonCalories + " kcal");
        lunchCaloriesText.setText("Calories: " + lunchCalories + " kcal");
        dinnerCaloriesText.setText("Calories: " + dinnerCalories + " kcal");
        totalCaloriesText.setText(totalCalo + " kcal");
        dateText.setText(currentDate);

        // Khởi tạo nút thêm bữa sáng và thêm sự kiện click
        ImageView btnAddBreakfast = findViewById(R.id.btnAddBreakfast);
        btnAddBreakfast.setOnClickListener(v -> {
            Intent intent = new Intent(DietaryHabit.this, AddBreakfastActivity.class);
            startActivity(intent);
        });

        ImageView btnAddAfternoon = findViewById(R.id.btnAddAfternoon);
        btnAddAfternoon.setOnClickListener(v -> {
            Intent intent = new Intent(DietaryHabit.this, AddAfternoonActivity.class);
            startActivity(intent);
        });

        ImageView btnAddLunch = findViewById(R.id.btnAddLunch);
        btnAddLunch.setOnClickListener(v -> {
            Intent intent = new Intent(DietaryHabit.this, AddLunchActivity.class);
            startActivity(intent);
        });

        ImageView btnAddDinner = findViewById(R.id.btnaddDinner);
        btnAddDinner.setOnClickListener(v -> {
            Intent intent = new Intent(DietaryHabit.this, AddDinnerActivity.class);
            startActivity(intent);
        });

        // Cài đặt Edge to Edge
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}