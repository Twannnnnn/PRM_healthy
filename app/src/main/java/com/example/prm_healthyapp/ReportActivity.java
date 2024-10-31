package com.example.prm_healthyapp;

import android.database.Cursor;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import androidx.viewpager2.widget.ViewPager2;

import java.util.ArrayList;
import java.util.List;

public class ReportActivity extends AppCompatActivity {
    DatabaseHelper dbHelper;

    LogAdapter logAdapter;
    List<LogItem> logList;
    TabLayout tabLayout;
    ViewPager2 viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        dbHelper = new DatabaseHelper(this);

        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);

        logList = new ArrayList<>();
        displayAllLogs(1); // Replace 1 with the actual userId

        ViewPagerAdapter adapter = new ViewPagerAdapter(this);
        viewPager.setAdapter(adapter);

        // Set up TabLayout with ViewPager2
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            switch (position) {
                case 0:
                    tab.setText("Sleep Log");
                    break;
                case 1:
                    tab.setText("Meal Log");
                    break;
            }
        }).attach();
    }

    private void displayAllLogs(int userId) {
        Cursor cursor = dbHelper.getAllLogs(userId);
        while (cursor.moveToNext()) {
            int logTypeIndex = cursor.getColumnIndex("log_type");
            int logDateIndex = cursor.getColumnIndex("log_date");
            int logTimeIndex = cursor.getColumnIndex("log_time");
            int titleIndex = cursor.getColumnIndex("title");
            int descriptionIndex = cursor.getColumnIndex("description");

            if (logTypeIndex != -1 && logTimeIndex != -1 && logDateIndex != -1
                    && titleIndex != -1 && descriptionIndex != -1) {
                String logType = cursor.getString(logTypeIndex);
                String logDate = cursor.getString(logDateIndex);
                String logTime = cursor.getString(logTimeIndex);
                String title = cursor.getString(titleIndex);
                String description = cursor.getString(descriptionIndex);

                // Create LogItem with all required parameters
                LogItem logItem = new LogItem(logType, logTime, logDate, title, description, ""); // Food mass can be empty for sleep logs
                logList.add(logItem);
            }
        }
        cursor.close();

        logAdapter = new LogAdapter(logList);
        // Set adapter to the RecyclerView (assuming it's done in the fragment)
    }
}