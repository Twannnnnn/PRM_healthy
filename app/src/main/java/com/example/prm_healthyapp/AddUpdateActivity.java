package com.example.prm_healthyapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;

public class AddUpdateActivity extends AppCompatActivity {

    private EditText editTextName, editTextDescription, editTextStartTime, editTextEndTime;
    private Button btnSave;
    private DatabaseHelper dbHelper;
    private int activityId = -1; // Default value for adding new activity

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_update);

        dbHelper = new DatabaseHelper(this);
        bindingView();
        bindingAction();
    }

    private void bindingView() {
        editTextName = findViewById(R.id.editTextName);
        editTextDescription = findViewById(R.id.editTextDescription);
        editTextStartTime = findViewById(R.id.editTextStartTime);
        editTextEndTime = findViewById(R.id.editTextEndTime);
        btnSave = findViewById(R.id.btnSave);
    }

    private void bindingAction() {
        Intent intent = getIntent();
        activityId = intent.getIntExtra("activityId", -1);
        if (activityId != -1) {
            loadActivityData(activityId);
        }

        btnSave.setOnClickListener(view -> {
            if (activityId != -1) {
                updateActivity();
            } else {
                addActivity();
            }
        });
    }

    private void loadActivityData(int id) {
        // Load activity data from database and set to EditTexts
        // Assuming you have a method to get activity details by ID
        ActivityModel activity = dbHelper.getActivityById(id);
        editTextName.setText(activity.getName());
        editTextDescription.setText(activity.getDescription());
        editTextStartTime.setText(activity.getStartTime());
        editTextEndTime.setText(activity.getEndTime());
    }

    private void addActivity() {
        // Add a new activity
        String name = editTextName.getText().toString();
        String description = editTextDescription.getText().toString();
        String startTime = editTextStartTime.getText().toString();
        String endTime = editTextEndTime.getText().toString();

        dbHelper.addActivity(1, name, description, startTime, endTime); // Assuming user_id is 1 for demo
        finish();
    }

    private void updateActivity() {
        // Update existing activity
        String name = editTextName.getText().toString();
        String description = editTextDescription.getText().toString();
        String startTime = editTextStartTime.getText().toString();
        String endTime = editTextEndTime.getText().toString();

        dbHelper.updateActivity(activityId, name, description, startTime, endTime);
        finish();
    }
}