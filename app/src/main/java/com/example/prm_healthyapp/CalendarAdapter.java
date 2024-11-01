package com.example.prm_healthyapp;

import android.content.Context;
import android.content.Intent;
import android.view.ViewGroup;
import android.widget.CalendarView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.Calendar;
import java.util.List;

public class CalendarAdapter extends RecyclerView.Adapter<CalendarAdapter.CalendarViewHolder> {

    private final List<Calendar> months;
    private final Context context;

    public CalendarAdapter(List<Calendar> months, Context context) {
        this.months = months;
        this.context = context;
    }

    @NonNull
    @Override
    public CalendarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CalendarView calendarView = new CalendarView(parent.getContext());
        calendarView.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        return new CalendarViewHolder(calendarView);
    }

    @Override
    public void onBindViewHolder(@NonNull CalendarViewHolder holder, int position) {
        Calendar calendar = months.get(position);
        holder.bind(calendar);

        holder.calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            // Định dạng lại ngày tháng theo dạng d/M/yyyy
            String formattedDate = dayOfMonth + "/" + (month + 1) + "/" + year; // (month + 1) vì tháng bắt đầu từ 0

            Intent intent = new Intent(context, DailyMealActivity.class);
            intent.putExtra("selectedDate", formattedDate);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return months.size();
    }

    static class CalendarViewHolder extends RecyclerView.ViewHolder {
        CalendarView calendarView;

        public CalendarViewHolder(@NonNull CalendarView itemView) {
            super(itemView);
            calendarView = itemView;
        }

        public void bind(Calendar calendar) {
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            calendarView.setDate(calendar.getTimeInMillis());
        }
    }
}
