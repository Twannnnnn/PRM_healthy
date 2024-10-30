package com.example.prm_healthyapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class LogAdapter extends RecyclerView.Adapter<LogAdapter.LogViewHolder> {
    private final List<LogItem> logList;
    private OnItemClickListener listener; // Listener for item clicks

    public LogAdapter(List<LogItem> logList) {
        this.logList = logList;
    }

    @NonNull
    @Override
    public LogViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.log_item, parent, false);
        return new LogViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LogViewHolder holder, int position) {
        LogItem logItem = logList.get(position);
        holder.tvLogType.setText(logItem.getLogType());
        holder.tvLogTime.setText(logItem.getLogTime());
        holder.tvTitle.setText(logItem.getTitle());
        holder.tvDescription.setText(logItem.getDescription());

        // Set click listener on the item view
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(logItem); // Call the listener
            }
        });
    }

    @Override
    public int getItemCount() {
        return logList.size();
    }

    public static class LogViewHolder extends RecyclerView.ViewHolder {
        TextView tvLogType, tvLogTime, tvTitle, tvDescription;

        public LogViewHolder(@NonNull View itemView) {
            super(itemView);
            tvLogType = itemView.findViewById(R.id.tvLogType);
            tvLogTime = itemView.findViewById(R.id.tvLogTime);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvDescription = itemView.findViewById(R.id.tvDescription);
        }
    }

    // Interface for item clicks
    public interface OnItemClickListener {
        void onItemClick(LogItem logItem);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener; // Set the listener
    }
}