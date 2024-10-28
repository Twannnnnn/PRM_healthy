package com.example.prm_healthyapp;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class SleepLogListActivity extends AppCompatActivity implements SleepLogAdapter.OnSleepLogListener {

    private TextView tvTotalSleepDuration;
    private Button btnAddSleepLog;
    private RecyclerView recyclerViewSleepLogs;
    private SleepLogAdapter sleepLogAdapter;
    private DatabaseHelper dbHelper;
    private List<SleepLogModel> sleepLogList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sleep_log_list);
        dbHelper = new DatabaseHelper(this);

        bindingView();
        bindingAction();
    }

    private void bindingView() {
        tvTotalSleepDuration = findViewById(R.id.tvTotalSleepDuration);
        btnAddSleepLog = findViewById(R.id.btnAddSleepLog);
        recyclerViewSleepLogs = findViewById(R.id.recyclerViewSleepLogs);
        recyclerViewSleepLogs.setLayoutManager(new LinearLayoutManager(this));
        sleepLogList = new ArrayList<>();
        sleepLogAdapter = new SleepLogAdapter(sleepLogList, this);
        recyclerViewSleepLogs.setAdapter(sleepLogAdapter);
    }

    private void bindingAction() {
        btnAddSleepLog.setOnClickListener(view -> {
            Intent intent = new Intent(SleepLogListActivity.this, AddUpdateSleepLogActivity.class);
            startActivity(intent);
        });

        loadSleepLogs();
        calculateTotalSleepDuration();
    }

    private void loadSleepLogs() {
        sleepLogList.clear();
        Cursor cursor = dbHelper.getAllSleepLogs(); // Fetch all sleep logs
        while (cursor.moveToNext()) {
            SleepLogModel sleepLog = new SleepLogModel(
                    cursor.getInt(cursor.getColumnIndex("id")),
                    cursor.getInt(cursor.getColumnIndex("user_id")),
                    cursor.getString(cursor.getColumnIndex("sleep_start")),
                    cursor.getString(cursor.getColumnIndex("sleep_end")),
                    cursor.getFloat(cursor.getColumnIndex("duration")),
                    cursor.getString(cursor.getColumnIndex("log_date"))
            );
            sleepLogList.add(sleepLog);
        }
        cursor.close();
        sleepLogAdapter.notifyDataSetChanged();
    }

    private void calculateTotalSleepDuration() {
        // Calculate total sleep duration and update the TextView
        float totalDuration = 0;
        for (SleepLogModel log : sleepLogList) {
            totalDuration += log.getDuration();
        }
        tvTotalSleepDuration.setText("Total Sleep Duration: " + totalDuration + " hours");
    }

    @Override
    public void onDeleteClick(int position) {
        SleepLogModel sleepLog = sleepLogList.get(position);
        dbHelper.deleteSleepLog(sleepLog.getId());
        loadSleepLogs();
        calculateTotalSleepDuration();
    }

    @Override
    public void onUpdateClick(int position) {
        SleepLogModel sleepLog = sleepLogList.get(position);
        Intent intent = new Intent(SleepLogListActivity.this, AddUpdateSleepLogActivity.class);
        intent.putExtra("sleepLogId", sleepLog.getId());
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadSleepLogs();
        calculateTotalSleepDuration();
    }
}