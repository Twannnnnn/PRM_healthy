package com.example.prm_healthyapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ActivityAdapter extends RecyclerView.Adapter<ActivityAdapter.ActivityViewHolder> {

    private List<ActivityModel> activityList;
    private OnActivityListener onActivityListener;

    public interface OnActivityListener {
        void onDeleteClick(int position);
        void onUpdateClick(int position);
    }

    public ActivityAdapter(List<ActivityModel> activityList, OnActivityListener listener) {
        this.activityList = activityList;
        this.onActivityListener = listener;
    }

    @NonNull
    @Override
    public ActivityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_item, parent, false);
        return new ActivityViewHolder(view, onActivityListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ActivityViewHolder holder, int position) {
        ActivityModel activity = activityList.get(position);
        holder.textViewActivityName.setText(activity.getName());
    }

    @Override
    public int getItemCount() {
        return activityList.size();
    }

    static class ActivityViewHolder extends RecyclerView.ViewHolder {
        TextView textViewActivityName;
        Button btnUpdate;
        Button btnDelete;

        public ActivityViewHolder(@NonNull View itemView, OnActivityListener listener) {
            super(itemView);
            textViewActivityName = itemView.findViewById(R.id.textViewActivityName);
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