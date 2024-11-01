package com.example.prm_healthyapp;

public class Meal {
    private int id;
    private int userId;
    private String mealType;
    private String planned_food_items;
    private String planned_calories;
    private String plan_date;

    public Meal(int id, int userId, String mealType, String planned_food_items, String planned_calories, String plan_date) {
        this.id = id;
        this.userId = userId;
        this.mealType = mealType;
        this.planned_food_items = planned_food_items;
        this.planned_calories = planned_calories;
        this.plan_date = plan_date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getMealType() {
        return mealType;
    }

    public void setMealType(String mealType) {
        this.mealType = mealType;
    }

    public String getPlanned_food_items() {
        return planned_food_items;
    }

    public void setPlanned_food_items(String planned_food_items) {
        this.planned_food_items = planned_food_items;
    }

    public String getPlanned_calories() {
        return planned_calories;
    }

    public void setPlanned_calories(String planned_calories) {
        this.planned_calories = planned_calories;
    }

    public String getPlan_date() {
        return plan_date;
    }

    public void setPlan_date(String plan_date) {
        this.plan_date = plan_date;
    }


}
