package com.example.prm_healthyapp;

import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class DailyMealActivity extends AppCompatActivity {

    private RecyclerView mealRecyclerView;
    private MealAdapter mealAdapter;
    private List<Meal> mealList = new ArrayList<>();
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_meal);

        TextView selectedDateText = findViewById(R.id.selectedDateText);
        mealRecyclerView = findViewById(R.id.mealRecyclerView);
        mealRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Get selected date from Intent
        String selectedDate = getIntent().getStringExtra("selectedDate");
        selectedDateText.setText("Meals for: " + selectedDate);
        int userId = getIntent().getIntExtra("userId", 1);

        // Initialize DatabaseHelper and fetch meals
        databaseHelper = new DatabaseHelper(this);
        mealList = databaseHelper.getMealsForDate(selectedDate,userId);



        // Set up adapter
        mealAdapter = new MealAdapter(mealList);
        mealRecyclerView.setAdapter(mealAdapter);
    }
}