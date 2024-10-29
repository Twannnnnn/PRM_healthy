package com.example.prm_healthyapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class UserInfActivity extends AppCompatActivity {

    private EditText editTextName, editTextEmail, editTextAge, editTextGender;
    private EditText editTextWeight, editTextHeight, editTextWaist, editTextNeck, editTextHips;
    private Button buttonSaveUser;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_userinf);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        db = new DatabaseHelper(this);

        bindingView();    // Call to bind views
        bindingAction();  // Call to bind actions
    }

    private void bindingView() {
        // Initialize UI components
        editTextName = findViewById(R.id.editTextName);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextAge = findViewById(R.id.editTextAge);
        editTextGender = findViewById(R.id.editTextGender);
        editTextWeight = findViewById(R.id.editTextWeight);
        editTextHeight = findViewById(R.id.editTextHeight);
        editTextWaist = findViewById(R.id.editTextWaist);
        editTextNeck = findViewById(R.id.editTextNeck);
        editTextHips = findViewById(R.id.editTextHips);
        buttonSaveUser = findViewById(R.id.buttonSave);
    }

    private void bindingAction() {
        // Set up button click listener
        buttonSaveUser.setOnClickListener(view -> saveUserData());
    }

    private void saveUserData() {
        // Collect data from EditTexts
        String name = editTextName.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        int age;
        double weight, height, waist, neck, hips = 0;

        // Validate user input
        if (name.isEmpty() || email.isEmpty() || editTextAge.getText().toString().isEmpty() ||
                editTextWeight.getText().toString().isEmpty() || editTextHeight.getText().toString().isEmpty() ||
                editTextWaist.getText().toString().isEmpty() || editTextNeck.getText().toString().isEmpty()) {
            Toast.makeText(this, "Please fill all required fields", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            age = Integer.parseInt(editTextAge.getText().toString());
            weight = Double.parseDouble(editTextWeight.getText().toString());
            height = Double.parseDouble(editTextHeight.getText().toString());
            waist = Double.parseDouble(editTextWaist.getText().toString());
            neck = Double.parseDouble(editTextNeck.getText().toString());
            // Optional hip measurement for females
            if (!editTextHips.getText().toString().isEmpty()) {
                hips = Double.parseDouble(editTextHips.getText().toString());
            }
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid input . Please check your numbers.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Calculate BMI
        double bmi = weight / Math.pow(height / 100, 2);  // height in meters

        // Calculate body fat percentage based on gender
        String gender = editTextGender.getText().toString().trim().toLowerCase();
        double bodyFat;
        if (gender.equals("male")) {
            bodyFat = 495 / (1.0324 - 0.19077 * Math.log10(waist - neck) + 0.15456 * Math.log10(height)) - 450;
        } else if (gender.equals("female")) {
            bodyFat = 495 / (1.29579 - 0.35004 * Math.log10(waist + hips - neck) + 0.22100 * Math.log10(height)) - 450;
        } else {
            Toast.makeText(this, "Invalid gender. Please enter 'male' or 'female'.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create User object
        User user = new User(null, name, email, "password", age, gender, weight, height, waist, neck, hips, bmi, bodyFat);

        // Save the user to the database
        saveUserToDatabase(user);
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void saveUserToDatabase(User user) {
        // Code to save user object to the database
        db.insertUser(user);
        Toast.makeText(this, "User saved successfully!", Toast.LENGTH_SHORT).show(); // Temporary success message
    }


}
