package com.example.prm_healthyapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.FoodViewHolder> {
    private List<FoodItem> foodItems;

    public FoodAdapter(List<FoodItem> foodItems) {
        this.foodItems = foodItems;
    }

    @NonNull
    @Override
    public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_food, parent, false);
        return new FoodViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodViewHolder holder, int position) {
        FoodItem foodItem = foodItems.get(position);
        holder.foodName.setText(foodItem.getName());
        holder.foodFat.setText(String.format("%.1f calo", foodItem.getTotal_calories()));
        holder.quantity = foodItem.getQuantity();  // Initialize with existing quantity
        holder.foodQuantity.setText(String.valueOf(holder.quantity));
        holder.foodMass.setText(" / 100g");


        // Thiết lập sự kiện cho nút cộng
        holder.addNewFoodButton.setOnClickListener(v -> {
            holder.quantity++;
            foodItem.setQuantity(holder.quantity); // Save updated quantity in FoodItem
            holder.foodQuantity.setText(String.valueOf(holder.quantity));
        });
        // Sự kiện cho nút trừ
        holder.subtractFoodButton.setOnClickListener(v -> {
            if (holder.quantity > 0) {  // Chỉ giảm nếu số lượng > 0
                holder.quantity--;
                foodItem.setQuantity(holder.quantity);
                holder.foodQuantity.setText(String.valueOf(holder.quantity));
            }
        });
    }

    @Override
    public int getItemCount() {
        return foodItems.size();
    }

    public List<FoodItem> getItemsWithQuantityGreaterThanOne() {
        List<FoodItem> itemsWithQuantity = new ArrayList<>();
        for (FoodItem foodItem : foodItems) {
            if (foodItem.getQuantity() > 0) {
                itemsWithQuantity.add(foodItem);
            }
        }
        return itemsWithQuantity;
    }

    static class FoodViewHolder extends RecyclerView.ViewHolder {
        TextView foodName, foodFat, foodQuantity, foodMass;
        ImageView addNewFoodButton,subtractFoodButton;
        int quantity = 0; // Bắt đầu số lượng mặc định là 0

        public FoodViewHolder(@NonNull View itemView) {
            super(itemView);
            foodName = itemView.findViewById(R.id.food_name);
            foodFat = itemView.findViewById(R.id.foot_fat);
            foodQuantity = itemView.findViewById(R.id.food_quantity); // Ánh xạ TextView cho số lượng
            foodMass = itemView.findViewById(R.id.food_mass);
            addNewFoodButton = itemView.findViewById(R.id.addNewFood); // Ánh xạ ImageView cho nút cộng
            subtractFoodButton = itemView.findViewById(R.id.subtractFood);
        }
    }
}
