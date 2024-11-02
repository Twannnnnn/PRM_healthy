package com.example.prm_healthyapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class MealPlan extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_plan);

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

        RecyclerView calendarRecyclerView = findViewById(R.id.calendarRecyclerView);
        calendarRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Tạo danh sách 12 tháng bắt đầu từ tháng hiện tại
        List<Calendar> months = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        for (int i = 0; i < 12; i++) {
            Calendar month = (Calendar) calendar.clone();
            month.add(Calendar.MONTH, i);
            months.add(month);
        }

        // Thiết lập adapter cho RecyclerView
        CalendarAdapter adapter = new CalendarAdapter(months, this);
        calendarRecyclerView.setAdapter(adapter);
    }
}

