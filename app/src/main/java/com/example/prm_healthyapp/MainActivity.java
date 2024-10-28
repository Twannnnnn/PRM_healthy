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
        btnUserInfo = findViewById(R.id.btnUserInfo);  // Initialize the new button

        btnGoToReport.setOnClickListener(this::onAction);
        btnUserInfo.setOnClickListener(this::onUserInfoAction);  // Set the click listener
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
}