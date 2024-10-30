package com.example.prm_healthyapp;

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
    private DatabaseHelper dbHelper;
    private RecyclerView recyclerView;
    private LogAdapter logAdapter;
    private List<LogItem> logList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sleep_log, container, false);

        dbHelper = new DatabaseHelper(getActivity());
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        logList = new ArrayList<>();
        displaySleepLogs(1); // Thay 1 bằng userId thực tế của bạn
        return view;
    }

    private void displaySleepLogs(int userId) {
        Cursor cursor = dbHelper.getAllSleepLogs(userId);

        while (cursor.moveToNext()) {
            String logTime = cursor.getString(cursor.getColumnIndex("sleep_start"));
            String title = "Giấc ngủ";
            String description = cursor.getString(cursor.getColumnIndex("sleep_end"));

            LogItem logItem = new LogItem("sleep", logTime, title, description);
            logList.add(logItem);
        }
        cursor.close();

        logAdapter = new LogAdapter(logList);
        recyclerView.setAdapter(logAdapter);
    }
}