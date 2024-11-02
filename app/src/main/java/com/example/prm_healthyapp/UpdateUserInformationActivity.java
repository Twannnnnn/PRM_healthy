package com.example.prm_healthyapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class UpdateUserInformationActivity extends AppCompatActivity {

    private EditText editTextName, editTextEmail, editTextAge, editTextGender, editTextWeight, editTextHeight, editTextWaist, editTextNeck, editTextHips;
    private Button buttonSave;
    private DatabaseHelper dbHelper;
    private User currentUser; // To hold the user details

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_user);

        dbHelper = new DatabaseHelper(this);
        currentUser = dbHelper.getFirstUser(); // Get the current user

        // Initialize UI elements
        editTextName = findViewById(R.id.editTextName);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextAge = findViewById(R.id.editTextAge);
        editTextGender = findViewById(R.id.editTextGender);
        editTextWeight = findViewById(R.id.editTextWeight);
        editTextHeight = findViewById(R.id.editTextHeight);
        editTextWaist = findViewById(R.id.editTextWaist);
        editTextNeck = findViewById(R.id.editTextNeck);
        editTextHips = findViewById(R.id.editTextHips);
        buttonSave = findViewById(R.id.buttonSave);

        // Populate the fields with current user data
        if (currentUser != null) {
            editTextName.setText(currentUser.getName());
            editTextEmail.setText(currentUser.getEmail());
            editTextAge.setText(String.valueOf(currentUser.getAge()));
            editTextGender.setText(currentUser.getGender());
            editTextWeight.setText(String.valueOf(currentUser.getWeight()));
            editTextHeight.setText(String.valueOf(currentUser.getHeight()));
            editTextWaist.setText(String.valueOf(currentUser.getWaist()));
            editTextNeck.setText(String.valueOf(currentUser.getNeck()));
            editTextHips.setText(String.valueOf(currentUser.getHips()));
        }

        // Set up the save button listener
        buttonSave.setOnClickListener(v -> updateUserInfo());
    }

    private void updateUserInfo() {
        String name = editTextName.getText().toString();
        String email = editTextEmail.getText().toString();
        Integer age = Integer.parseInt(editTextAge.getText().toString());
        String gender = editTextGender.getText().toString();
        Float weight = Float.parseFloat(editTextWeight.getText().toString());
        double height = Double.parseDouble(editTextHeight.getText().toString());
        double waist = Double.parseDouble(editTextWaist.getText().toString());
        double neck = Double.parseDouble(editTextNeck.getText().toString());
        double hips = Double.parseDouble(editTextHips.getText().toString());



        if (currentUser != null) {
            currentUser.setName(name);
            currentUser.setEmail(email);
            currentUser.setAge(age);
            currentUser.setGender(gender);
            currentUser.setWeight(weight);
            currentUser.setHeight(height);
            currentUser.setWaist(waist);
            currentUser.setNeck(neck);
            currentUser.setHips(hips);

            double bmi = weight / Math.pow(height / 100, 2);  // height in meters

            double bodyFat;
            if (gender.equals("male")) {
                bodyFat = 495 / (1.0324 - 0.19077 * Math.log10(waist - neck) + 0.15456 * Math.log10(height)) - 450;
            } else if (gender.equals("female")) {
                bodyFat = 495 / (1.29579 - 0.35004 * Math.log10(waist + hips - neck) + 0.22100 * Math.log10(height)) - 450;
            } else {
                Toast.makeText(this, "Invalid gender. Please enter 'male' or 'female'.", Toast.LENGTH_SHORT).show();
                return;
            }

            currentUser.setBodyFatPercentage(bodyFat);
            currentUser.setBmi(bmi);
            dbHelper.updateUser(currentUser); // Update user information without changing password
            Toast.makeText(this, "User information updated successfully!", Toast.LENGTH_SHORT).show();
            finish(); // Close this activity and return to the previous one
        } else {
            Toast.makeText(this, "User not found!", Toast.LENGTH_SHORT).show();
        }
    }
}