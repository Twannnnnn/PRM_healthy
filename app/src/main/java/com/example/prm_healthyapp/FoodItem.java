package com.example.prm_healthyapp;

public class FoodItem {
    private int id;
    private String name;
    private double fat;
    private double protein;
    private double carbohydrates;
    private double fiber;
    private String vitamins;
    private String minerals;
    private double total_calories;
    private int quantity;

    // Constructor
    public FoodItem(int id, String name, double fat, double protein, double carbohydrates, double fiber, String vitamins, String minerals, double total_calories, int quantity) {
        this.id = id;
        this.name = name;
        this.fat = fat;
        this.protein = protein;
        this.carbohydrates = carbohydrates;
        this.fiber = fiber;
        this.vitamins = vitamins;
        this.minerals = minerals;
        this.total_calories = total_calories;
        this.quantity = quantity;
    }

    // Getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public double getFat() { return fat; }
    public void setFat(double fat) { this.fat = fat; }

    public double getProtein() { return protein; }
    public void setProtein(double protein) { this.protein = protein; }

    public double getCarbohydrates() { return carbohydrates; }
    public void setCarbohydrates(double carbohydrates) { this.carbohydrates = carbohydrates; }

    public double getFiber() { return fiber; }
    public void setFiber(double fiber) { this.fiber = fiber; }

    public String getVitamins() { return vitamins; }
    public void setVitamins(String vitamins) { this.vitamins = vitamins; }

    public String getMinerals() { return minerals; }
    public void setMinerals(String minerals) { this.minerals = minerals; }

    public double getTotal_calories() { return protein; }
    public void setTotal_calories(double protein) { this.protein = protein; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

}

