package com.example.prm_healthyapp;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ReportActivity extends AppCompatActivity {
    DatabaseHelper dbHelper;
    TextView tvTotalCalories, tvMealLogs, tvMealDetails, tvTotalSleepDuration, tvSleepLogs, tvSleepDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_report);

        // Khởi tạo DatabaseHelper
        dbHelper = new DatabaseHelper(this);

        // Khởi tạo các TextView
        tvTotalCalories = findViewById(R.id.tvTotalCalories);
        tvMealLogs = findViewById(R.id.tvMealLogs);
        tvMealDetails = findViewById(R.id.tvMealDetails);
        tvTotalSleepDuration = findViewById(R.id.tvTotalSleepDuration);
        tvSleepLogs = findViewById(R.id.tvSleepLogs);
        tvSleepDetails = findViewById(R.id.tvSleepDetails);

        // Nhập dữ liệu giả
        insertSampleData();

        // Hiển thị dữ liệu bữa ăn
        displayMealData();

        // Hiển thị dữ liệu giấc ngủ
        displaySleepData();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void insertSampleData() {
        if (!isUserExists("a@example.com")) {
            dbHelper.addUser("Nguyen Van A", "a@example.com", "password123");
        }

        if (dbHelper.getAllMealLogs(1).getCount() == 0) {
            dbHelper.addMealLog(1, "Bữa sáng", "Bánh mì, Trứng", 300, "08:00", "2024-10-28");
            dbHelper.addMealLog(1, "Bữa trưa", "Cơm, Thịt gà", 500, "12:00", "2024-10-28");
            dbHelper.addMealLog(1, "Bữa tối", "Mì, Rau củ", 400, "18:00", "2024-10-28");
        }

        if (dbHelper.getAllSleepLogs(1).getCount() == 0) {
            dbHelper.addSleepLog(1, "22:00", "06:00", 8, "2024-10-28");
            dbHelper.addSleepLog(1, "23:00", "07:00", 8, "2024-10-29");
        }
    }

    private boolean isUserExists(String email) {
        Cursor cursor = dbHelper.getReadableDatabase().rawQuery("SELECT * FROM users WHERE email = ?", new String[]{email});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    private void displayMealData() {
        Cursor mealCursor = dbHelper.getAllMealLogs(1);
        float totalCalories = dbHelper.getTotalCalories(1);
        tvTotalCalories.setText("Tổng calo: " + totalCalories);

        StringBuilder mealLogs = new StringBuilder();
        while (mealCursor.moveToNext()) {
            int mealTypeIndex = mealCursor.getColumnIndex("meal_type");
            int foodItemsIndex = mealCursor.getColumnIndex("food_items");

            if (mealTypeIndex != -1 && foodItemsIndex != -1) {
                String mealType = mealCursor.getString(mealTypeIndex);
                String foodItems = mealCursor.getString(foodItemsIndex);
                mealLogs.append(mealType).append(": ").append(foodItems).append("\n");
            }
        }
        tvMealDetails.setText(mealLogs.toString());
        mealCursor.close();
    }

    private void displaySleepData() {
        Cursor sleepCursor = dbHelper.getAllSleepLogs(1);
        float totalSleepDuration = dbHelper.getTotalSleepDuration(1);
        tvTotalSleepDuration.setText("Tổng thời gian ngủ: " + totalSleepDuration + " giờ");

        StringBuilder sleepLogs = new StringBuilder();
        while (sleepCursor.moveToNext()) {
            int sleepStartIndex = sleepCursor.getColumnIndex("sleep_start");
            int sleepEndIndex = sleepCursor.getColumnIndex("sleep_end");

            if (sleepStartIndex != -1 && sleepEndIndex != -1) {
                String sleepStart = sleepCursor.getString(sleepStartIndex);
                String sleepEnd = sleepCursor.getString(sleepEndIndex);
                sleepLogs.append("Bắt đầu: ").append(sleepStart).append(", Kết thúc: ").append(sleepEnd).append("\n");
            }
        }
        tvSleepDetails.setText(sleepLogs.toString());
        sleepCursor.close();
    }
}