package com.example.prm_healthyapp;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private Button btnGoToReport, btnAddFood,AichatButton;
    private Button btnUserInfo;
    private Button btnActivities;
    private Button btnGoToSleepLog;
    private Button btnDietaryHabit;
    DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbHelper = new DatabaseHelper(this);

        btnGoToReport = findViewById(R.id.btnGoToReport);
        btnAddFood = findViewById(R.id.btnAddFood);
        AichatButton = findViewById(R.id.Aichat);
        btnAddFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddFoodDialog();
            }
        });
        AichatButton.setOnClickListener(this::onActionChat);
        checkUser();
        initUI();
    }
    private void onActionChat(View view) {
        // Navigate to ReportActivity
        Intent intent = new Intent(MainActivity.this, HealthAdviceActivity.class);
        startActivity(intent);
    }
    private void showAddFoodDialog() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_add_food);
        dialog.setCancelable(true);

        EditText editTextFoodName = dialog.findViewById(R.id.editTextFoodName);
        EditText editTextFat = dialog.findViewById(R.id.editTextFat);
        EditText editTextProtein = dialog.findViewById(R.id.editTextProtein);
        EditText editTextCarbohydrates = dialog.findViewById(R.id.editTextCarbohydrates);
        EditText editTextFiber = dialog.findViewById(R.id.editTextFiber);
        Button buttonSaveFood = dialog.findViewById(R.id.buttonSaveFood);

        buttonSaveFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editTextFoodName.getText().toString();
                float fat = Float.parseFloat(editTextFat.getText().toString());
                float protein = Float.parseFloat(editTextProtein.getText().toString());
                float carbohydrates = Float.parseFloat(editTextCarbohydrates.getText().toString());
                float fiber = Float.parseFloat(editTextFiber.getText().toString());

                // Gọi phương thức để lưu thực phẩm vào cơ sở dữ liệu
                // dbHelper.addFood(name, fat, protein, carbohydrates, fiber, vitamins, minerals);

                Toast.makeText(MainActivity.this, "Thực phẩm đã được thêm", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

        dialog.show();
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

    private void initUI() {
        btnGoToReport = findViewById(R.id.btnGoToReport);
        btnUserInfo = findViewById(R.id.btnUserInfo);
        btnActivities = findViewById(R.id.btnGoToActivities);
        btnGoToSleepLog = findViewById(R.id.btnGoToSleepLog);// Initialize the new button
        btnDietaryHabit = findViewById(R.id.btnDietaryHabit);


        btnGoToReport.setOnClickListener(this::onAction);
        btnUserInfo.setOnClickListener(this::onUserInfoAction);
        btnActivities.setOnClickListener(this::onActivities);
        btnGoToSleepLog.setOnClickListener(this::onSleepLog);// Set the click listener
        btnDietaryHabit.setOnClickListener(this::onDietaryHabit);
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
        // Navigate to UserInfoActivity
        Intent intent = new Intent(MainActivity.this, ActivityListActivity.class);
        startActivity(intent);
    }

    private void onSleepLog(View view) {
        // Navigate to UserInfoActivity
        Intent intent = new Intent(MainActivity.this, SleepLogListActivity.class);
        startActivity(intent);
    }

    private void onDietaryHabit(View view) {
        // Navigate to UserInfoActivity
        Intent intent = new Intent(MainActivity.this, DietaryHabit.class);
        startActivity(intent);
    }


}