package com.example.prm_healthyapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.gridlayout.widget.GridLayout;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class MealPlan extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_plan);

        // Set padding để tránh bị che khuất bởi thanh hệ thống
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ImageView addMealPlan = findViewById(R.id.addMealPlanButton);
        addMealPlan.setOnClickListener(v -> {
            Intent intent = new Intent(MealPlan.this, AddMealPlanActivity.class);
            startActivity(intent);
        });

        // Tìm LinearLayout calendarContainer trong layout
        GridLayout calendarContainer = findViewById(R.id.calendarContainer);

        // Định dạng ngày
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd\nEE", Locale.getDefault());
        Calendar calendar = Calendar.getInstance();

        // Tạo các TextView cho 7 ngày tiếp theo
        for (int i = 0; i < 7; i++) {
            // Tạo TextView cho mỗi ngày
            TextView dayTextView = new TextView(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(8, 0, 8, 0); // Tạo khoảng cách giữa các ngày

            dayTextView.setLayoutParams(params);
            dayTextView.setTextAlignment(TextView.TEXT_ALIGNMENT_CENTER);
            dayTextView.setTextColor(ContextCompat.getColor(this, android.R.color.white));
            dayTextView.setPadding(8, 8, 8, 8);
            dayTextView.setText(dateFormat.format(calendar.getTime()));

            // Thêm TextView vào calendarContainer
            calendarContainer.addView(dayTextView);

            // Tiến đến ngày tiếp theo
            calendar.add(Calendar.DAY_OF_YEAR, 1);
        }
    }
}
