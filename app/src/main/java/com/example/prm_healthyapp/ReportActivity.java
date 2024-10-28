package com.example.prm_healthyapp;

import android.database.Cursor;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ReportActivity extends AppCompatActivity {
    DatabaseHelper dbHelper;
    RecyclerView recyclerView;
    LogAdapter logAdapter;
    List<LogItem> logList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        dbHelper = new DatabaseHelper(this);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        logList = new ArrayList<>();
        displayAllLogs(1); // Thay 1 bằng userId thực tế của bạn
    }

    private void displayAllLogs(int userId) {
        Cursor cursor = dbHelper.getAllLogs(userId);

        while (cursor.moveToNext()) {
            int logTypeIndex = cursor.getColumnIndex("log_type");
            int logTimeIndex = cursor.getColumnIndex("log_time");
            int titleIndex = cursor.getColumnIndex("title");
            int descriptionIndex = cursor.getColumnIndex("description");

            // Kiểm tra xem chỉ số cột có hợp lệ không
            if (logTypeIndex != -1 && logTimeIndex != -1 && titleIndex != -1 && descriptionIndex != -1) {
                String logType = cursor.getString(logTypeIndex);
                String logTime = cursor.getString(logTimeIndex);
                String title = cursor.getString(titleIndex);
                String description = cursor.getString(descriptionIndex);

                LogItem logItem = new LogItem(logType, logTime, title, description);
                logList.add(logItem);
            }
        }
        cursor.close();

        logAdapter = new LogAdapter(logList);
        recyclerView.setAdapter(logAdapter);
    }
}