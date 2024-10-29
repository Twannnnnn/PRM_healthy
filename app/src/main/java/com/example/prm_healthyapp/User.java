package com.example.prm_healthyapp;

public class User {
    private Integer id;                  // Primary Key
    private String name;                 // User's name
    private String email;                // User's email
    private String password;             // User's password (should be stored securely)
    private int age;                     // User's age
    private String gender;               // User's gender
    private double weight;               // Weight in kg
    private double height;               // Height in cm
    private double waist;                // Waist circumference in cm
    private double neck;                 // Neck circumference in cm
    private Double hips;                 // Hip circumference in cm (optional)
    private Double bmi;                  // Body Mass Index (optional)
    private Double bodyFatPercentage;    // Body fat percentage (optional)

    public User(Integer id) {
        this.id = id;
    }

    // Constructor
    public User(Integer id, String name, String email, String password, int age, String gender,
                double weight, double height, double waist, double neck, Double hips,
                Double bmi, Double bodyFatPercentage) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.age = age;
        this.gender = gender;
        this.weight = weight;
        this.height = height;
        this.waist = waist;
        this.neck = neck;
        this.hips = hips;
        this.bmi = bmi;
        this.bodyFatPercentage = bodyFatPercentage;
    }

    // Default Constructor
    public User() {}

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getWaist() {
        return waist;
    }

    public void setWaist(double waist) {
        this.waist = waist;
    }

    public double getNeck() {
        return neck;
    }

    public void setNeck(double neck) {
        this.neck = neck;
    }

    public Double getHips() {
        return hips;
    }

    public void setHips(Double hips) {
        this.hips = hips;
    }

    public Double getBmi() {
        return bmi;
    }

    public void setBmi(Double bmi) {
        this.bmi = bmi;
    }

    public Double getBodyFatPercentage() {
        return bodyFatPercentage;
    }

    public void setBodyFatPercentage(Double bodyFatPercentage) {
        this.bodyFatPercentage = bodyFatPercentage;
    }
}
