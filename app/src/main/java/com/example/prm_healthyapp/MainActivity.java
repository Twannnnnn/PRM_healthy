package com.example.prm_healthyapp;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private FrameLayout btnGoToReport, btnAddFood, btnUserInfo, btnGoToSleepLog, btnGoToActivities, btnAIChat,btnGoToHealthAdvice,btnMealPlan,btnSetNutritionGoal,btnDietaryHabit, btnShare, btnSync, btnExpert;

    private DatabaseHelper dbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbHelper = new DatabaseHelper(this);
        dbHelper.addDefaultSleepActivity();
        // Initialize FrameLayouts instead of Buttons
        btnGoToReport = findViewById(R.id.btnGoToReport);
        btnAIChat = findViewById(R.id.Aichat);
        btnUserInfo = findViewById(R.id.btnUserInfo);
        btnGoToActivities = findViewById(R.id.btnGoToActivities);
        btnGoToSleepLog = findViewById(R.id.btnGoToSleepLog);
        btnGoToHealthAdvice = findViewById(R.id.btnGetAdvice);
        btnMealPlan = findViewById(R.id.btnMealPlan);
        btnSetNutritionGoal = findViewById(R.id.btnSetNutritionGoal);
        btnDietaryHabit = findViewById(R.id.btnDietaryHabit);
        btnSync = findViewById(R.id.btnSync);
        btnShare = findViewById(R.id.btnShare);
        btnSync.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SyncActivity.class);
            startActivity(intent);
        });
        btnShare.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SharePlanActivity.class);
            startActivity(intent);
        });

        Intent intent = getIntent();
        if (intent != null && "start_sleep_log".equals(intent.getStringExtra("action"))) {
            String sleepStartTime = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());
            createSleepLog(sleepStartTime);
        }


        // Set up click listeners
        btnAIChat.setOnClickListener(this::onActionChat);
        btnGoToReport.setOnClickListener(this::onAction);
        btnUserInfo.setOnClickListener(this::onUserInfoAction);
        btnGoToActivities.setOnClickListener(this::onActivities);
        btnGoToSleepLog.setOnClickListener(this::onSleepLog);
        btnGoToHealthAdvice.setOnClickListener(this::onActionGoToHealthAdvice);
        btnMealPlan.setOnClickListener(this::onMealPlan);
        btnDietaryHabit.setOnClickListener(this::onDietaryHabit);
        btnSetNutritionGoal.setOnClickListener(this::onSetNutritionGoal);


        checkUser();
    }

    private void createSleepLog(String sleepStartTime) {
        int userId = dbHelper.getFirstUser().getId(); // Implement this method to get the user ID
        String logDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        // Add the sleep log to the database
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        dbHelper.addSleepLog(userId, sleepStartTime, sleepStartTime, 0.0f, logDate); // Duration can be set later
        Toast.makeText(this, "Sleep log created with start time: " + sleepStartTime, Toast.LENGTH_SHORT).show();
    }

    private void onActionGoToHealthAdvice(View view) {
        // Navigate to HealthAdviceActivity
        Intent intent = new Intent(MainActivity.this, HealthAdviceActivityLlama.class);
        startActivity(intent);
    }

    private void onActionChat(View view) {
        // Navigate to HealthAdviceActivity
        Intent intent = new Intent(MainActivity.this, HealthAdviceActivity.class);
        startActivity(intent);
    }

    private void checkUser() {
        User user = dbHelper.getFirstUser();

        if (user != null) {
            Toast.makeText(this, "User found: " + user.getName(), Toast.LENGTH_SHORT).show();
        } else {
            Intent intent = new Intent(this, UserInfActivity.class);
            startActivity(intent);
        }
    }


    private void onAction(View view) {
        // Navigate to ReportActivity
        Intent intent = new Intent(MainActivity.this, ReportActivity.class);
        startActivity(intent);
    }

    private void onUserInfoAction(View view) {
        // Navigate to UserInfoActivity
        Intent intent = new Intent(MainActivity.this, UserInfoActivity.class);
        startActivity(intent);
    }

    private void onActivities(View view) {
        // Navigate to ActivityListActivity
        Intent intent = new Intent(MainActivity.this, ActivityListActivity.class);
        startActivity(intent);
    }

    private void onSleepLog(View view) {
        // Navigate to SleepLogListActivity
        Intent intent = new Intent(MainActivity.this, SleepLogListActivity.class);
        startActivity(intent);
    }


    private void onDietaryHabit(View view) {
        // Navigate to UserInfoActivity
        Intent intent = new Intent(MainActivity.this, DietaryHabit.class);
        startActivity(intent);
    }

    private void onMealPlan(View view) {
        // Navigate to UserInfoActivity
        Intent intent = new Intent(MainActivity.this, MealPlan.class);
        startActivity(intent);
    }

    private void onSetNutritionGoal(View view) {
        // Navigate to UserInfoActivity
        Intent intent = new Intent(MainActivity.this, SetNutritionGoalsActivity.class);
        startActivity(intent);
    }



}