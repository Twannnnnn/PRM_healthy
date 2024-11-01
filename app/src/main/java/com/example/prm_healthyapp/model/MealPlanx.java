package com.example.prm_healthyapp.model;

public class MealPlanx {
    private int id;
    private int userId;
    private String mealType;
    private String plannedFoodItems;
    private float plannedCalories;
    private String planDate;

    public MealPlanx(int id, int userId, String mealType, String plannedFoodItems, float plannedCalories, String planDate) {
        this.id = id;
        this.userId = userId;
        this.mealType = mealType;
        this.plannedFoodItems = plannedFoodItems;
        this.plannedCalories = plannedCalories;
        this.planDate = planDate;
    }

    // Getters and toString() method for displaying
    public int getId() { return id; }
    public int getUserId() { return userId; }
    public String getMealType() { return mealType; }
    public String getPlannedFoodItems() { return plannedFoodItems; }
    public float getPlannedCalories() { return plannedCalories; }
    public String getPlanDate() { return planDate; }

    @Override
    public String toString() {
        return "MealPlan{" +
                "id=" + id +
                ", userId=" + userId +
                ", mealType='" + mealType + '\'' +
                ", plannedFoodItems='" + plannedFoodItems + '\'' +
                ", plannedCalories=" + plannedCalories +
                ", planDate='" + planDate + '\'' +
                '}';
    }
}
