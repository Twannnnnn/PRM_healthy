package com.example.prm_healthyapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class SleepLogAdapter extends RecyclerView.Adapter<SleepLogAdapter.SleepLogViewHolder> {

    private List<SleepLogModel> sleepLogList;
    private OnSleepLogListener onSleepLogListener;



    public interface OnSleepLogListener {
        void onDeleteClick(int position);
        void onUpdateClick(int position);
    }

    public SleepLogAdapter(List<SleepLogModel> sleepLogList, OnSleepLogListener listener) {
        this.sleepLogList = sleepLogList;
        this.onSleepLogListener = listener;
    }

    @NonNull
    @Override
    public SleepLogViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_sleep_log_item, parent, false);
        return new SleepLogViewHolder(view, onSleepLogListener);
    }

    @Override
    public void onBindViewHolder(@NonNull SleepLogViewHolder holder, int position) {
        SleepLogModel sleepLog = sleepLogList.get(position);
        holder.tvLogDate.setText(sleepLog.getLogDate());
        holder.tvSleepStart.setText("Sleep Start: " + sleepLog.getSleepStart());
        holder.tvSleepEnd.setText("Sleep End: " + sleepLog.getSleepEnd());
        holder.tvDuration.setText("Duration: " + sleepLog.getDuration() + " hours");
    }

    @Override
    public int getItemCount() {
        return sleepLogList.size();
    }

    static class SleepLogViewHolder extends RecyclerView.ViewHolder {
        TextView tvLogDate, tvSleepStart, tvSleepEnd, tvDuration;
        Button btnUpdate, btnDelete;

        public SleepLogViewHolder(@NonNull View itemView, OnSleepLogListener listener) {
            super(itemView);
            tvLogDate = itemView.findViewById(R.id.tvLogDate);
            tvSleepStart = itemView.findViewById(R.id.tvSleepStart);
            tvSleepEnd = itemView.findViewById(R.id.tvSleepEnd);
            tvDuration = itemView.findViewById(R.id.tvDuration);
            btnUpdate = itemView.findViewById(R.id.btnUpdate);
            btnDelete = itemView.findViewById(R.id.btnDelete);

            btnDelete.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onDeleteClick(getAdapterPosition());
                }
            });

            btnUpdate.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onUpdateClick(getAdapterPosition());
                }
            });
        }
    }
}