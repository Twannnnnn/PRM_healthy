package com.example.prm_healthyapp;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class SleepLogFragment extends Fragment {
    private RecyclerView recyclerView;
    private LogAdapter logAdapter;
    private List<LogItem> logList;
    private DatabaseHelper dbHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sleep_log, container, false);

        dbHelper = new DatabaseHelper(getActivity());
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        logList = new ArrayList<>();
        loadSleepLogs(1); // Replace 1 with the actual userId

        logAdapter = new LogAdapter(logList);
        recyclerView.setAdapter(logAdapter);

        return view;
    }

    private void loadSleepLogs(int userId) {
        Cursor cursor = dbHelper.getAllSleepLogs(userId);
        while (cursor.moveToNext()) {
            @SuppressLint("Range") String sleepStart = cursor.getString(cursor.getColumnIndex("sleep_start"));
            @SuppressLint("Range") String sleepEnd = cursor.getString(cursor.getColumnIndex("sleep_end"));
            @SuppressLint("Range") String logDate = cursor.getString(cursor.getColumnIndex("log_date")); // Retrieve log date
            @SuppressLint("Range") float duration = cursor.getFloat(cursor.getColumnIndex("duration"));

            String title = "Sleep Log"; // You can customize the title as needed
            String description = "Ended at: " + sleepEnd + "\nDuration: " + duration + " hours";

            // Create LogItem with all required parameters
            LogItem logItem = new LogItem("Sleep", sleepStart, logDate, title, description, ""); // Food mass can be empty for sleep logs
            logList.add(logItem);
        }

        if (cursor != null) {
            cursor.close();
        }
    }
}