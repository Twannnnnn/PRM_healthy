package com.example.prm_healthyapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    private Button btnGoToReport;
    private Button btnUserInfo;
    private Button btnActivities;
    private Button btnGoToSleepLog;
    DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        dbHelper = new DatabaseHelper(this);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize the UI components
        checkUser();
        initUI();
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

        btnGoToReport.setOnClickListener(this::onAction);
        btnUserInfo.setOnClickListener(this::onUserInfoAction);
        btnActivities.setOnClickListener(this::onActivities);
        btnGoToSleepLog.setOnClickListener(this::onSleepLog);// Set the click listener
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
}