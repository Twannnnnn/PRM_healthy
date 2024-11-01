package com.example.prm_healthyapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MealAdapter extends RecyclerView.Adapter<MealAdapter.MealViewHolder> {

    private List<Meal> mealList;

    public MealAdapter(List<Meal> mealList) {
        this.mealList = mealList;
    }

    @Override
    public MealViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_meal, parent, false);
        return new MealViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MealViewHolder holder, int position) {
        Meal meal = mealList.get(position);
        holder.mealTypeTextView.setText(meal.getMealType());
        holder.foodItemsTextView.setText(meal.getPlanned_food_items());
        try {
            float calories = Float.parseFloat(meal.getPlanned_calories());
            holder.caloriesTextView.setText(String.format("Calories: %.2f", calories));
        } catch (NumberFormatException e) {
            holder.caloriesTextView.setText("Calories: N/A");
        }
    }

    @Override
    public int getItemCount() {
        return mealList.size();
    }

    class MealViewHolder extends RecyclerView.ViewHolder {
        TextView mealTypeTextView, foodItemsTextView, caloriesTextView;

        MealViewHolder(View itemView) {
            super(itemView);
            mealTypeTextView = itemView.findViewById(R.id.mealTypeTextView);
            foodItemsTextView = itemView.findViewById(R.id.foodItemsTextView);
            caloriesTextView = itemView.findViewById(R.id.caloriesTextView);
        }
    }
}
