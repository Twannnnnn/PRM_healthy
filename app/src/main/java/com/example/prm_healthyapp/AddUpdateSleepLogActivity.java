package com.example.prm_healthyapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import androidx.appcompat.app.AppCompatActivity;

public class AddUpdateSleepLogActivity extends AppCompatActivity {

    private EditText editTextLogDate;
    private TimePicker timePickerStart, timePickerEnd;
    private Button btnSave;
    private DatabaseHelper dbHelper;
    private int sleepLogId = -1; // Default value for adding new sleep log

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_update_sleep_log);
        dbHelper = new DatabaseHelper(this);

        bindingView();
        bindingAction();
    }

    private void bindingView() {
        editTextLogDate = findViewById(R.id.editTextLogDate);
        timePickerStart = findViewById(R.id.timePickerStart);
        timePickerEnd = findViewById(R.id.timePickerEnd);
        btnSave = findViewById(R.id.btnSave);
    }

    private void bindingAction() {
        Intent intent = getIntent();
        sleepLogId = intent.getIntExtra("sleepLogId", -1);
        if (sleepLogId != -1) {
            loadSleepLogData(sleepLogId);
        }

        btnSave.setOnClickListener(view -> {
            if (sleepLogId != -1) {
                updateSleepLog();
            } else {
                addSleepLog();
            }
        });
    }

    private void loadSleepLogData(int id) {
        SleepLogModel sleepLog = dbHelper.getSleepLogById(id);
        if (sleepLog != null) {
            editTextLogDate.setText(sleepLog.getLogDate());
            String[] startTimeParts = sleepLog.getSleepStart().split(":");
            timePickerStart.setHour(Integer.parseInt(startTimeParts[0]));
            timePickerStart.setMinute(Integer.parseInt(startTimeParts[1]));

            String[] endTimeParts = sleepLog.getSleepEnd().split(":");
            timePickerEnd.setHour(Integer.parseInt(endTimeParts[0]));
            timePickerEnd.setMinute(Integer.parseInt(endTimeParts[1]));
        }
    }

    private void addSleepLog() {
        String logDate = editTextLogDate.getText().toString();
        String sleepStart = String.format("%02d:%02d", timePickerStart.getHour(), timePickerStart.getMinute());
        String sleepEnd = String.format("%02d:%02d", timePickerEnd.getHour(), timePickerEnd.getMinute());

        // Assuming duration can be calculated
        float duration = calculateDuration(sleepStart, sleepEnd);
        dbHelper.addSleepLog(1, sleepStart, sleepEnd, duration, logDate); // Assuming user_id is 1 for demo
        finish();
    }

    private void updateSleepLog() {
        String logDate = editTextLogDate.getText().toString();
        String sleepStart = String.format("%02d:%02d", timePickerStart.getHour(), timePickerStart.getMinute());
        String sleepEnd = String.format("%02d:%02d", timePickerEnd.getHour(), timePickerEnd.getMinute());
        float duration = calculateDuration(sleepStart, sleepEnd);

        dbHelper.updateSleepLog(sleepLogId, sleepStart, sleepEnd, duration, logDate);
        finish();
    }

    private float calculateDuration(String start, String end) {
        String[] startParts = start.split(":");
        String[] endParts = end.split(":");

        int startHour = Integer.parseInt(startParts[0]);
        int startMinute = Integer.parseInt(startParts[1]);
        int endHour = Integer.parseInt(endParts[0]);
        int endMinute = Integer.parseInt(endParts[1]);

        int totalStartMinutes = startHour * 60 + startMinute;
        int totalEndMinutes = endHour * 60 + endMinute;

        return (totalEndMinutes - totalStartMinutes) / 60.0f;
    }
}