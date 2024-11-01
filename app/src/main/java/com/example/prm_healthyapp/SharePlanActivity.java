package com.example.prm_healthyapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.prm_healthyapp.model.MealPlanx;

import java.util.List;

public class SharePlanActivity extends AppCompatActivity {

    LinearLayout ln1, ln2, ln3;
    DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_share_plan);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        dbHelper = new DatabaseHelper(this); // Initialize your DatabaseHelper here
        int userId = 1; // Replace with actual user ID
        List<MealPlanx> mealPlans = dbHelper.getMealPlansByUserId(userId);

        // Create a formatted string of meal plans to share
        StringBuilder shareTextBuilder = new StringBuilder("Check out this amazing health plan!\n\n");

        for (MealPlanx mealPlan : mealPlans) {
            shareTextBuilder.append("Meal Type: ").append(mealPlan.getMealType()).append("\n")
                    .append("Planned Food Items: ").append(mealPlan.getPlannedFoodItems()).append("\n")
                    .append("Planned Calories: ").append(mealPlan.getPlannedCalories()).append("\n")
                    .append("Plan Date: ").append(mealPlan.getPlanDate()).append("\n\n");
        }

        // Convert the StringBuilder to a String
        String shareText = shareTextBuilder.toString();

        ln1 = findViewById(R.id.ln1);
        ln2 = findViewById(R.id.ln2);
        ln3 = findViewById(R.id.ln3);

        // Handle share data through Gmail
        ln1.setOnClickListener(v -> {
            Intent emailIntent = new Intent(Intent.ACTION_SEND);
            emailIntent.setType("text/plain");
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Health Plan");
            emailIntent.putExtra(Intent.EXTRA_TEXT, shareText);
            try {
                startActivity(Intent.createChooser(emailIntent, "Share via Gmail"));
            } catch (android.content.ActivityNotFoundException ex) {
                Toast.makeText(SharePlanActivity.this, "No email clients installed.", Toast.LENGTH_SHORT).show();
            }
        });

        // Handle share data through Facebook
        ln2.setOnClickListener(v -> {
            Intent facebookIntent = new Intent(Intent.ACTION_SEND);
            facebookIntent.setType("text/plain");
            facebookIntent.putExtra(Intent.EXTRA_TEXT, shareText);
            facebookIntent.setPackage("com.facebook.katana");
            try {
                startActivity(facebookIntent);
            } catch (android.content.ActivityNotFoundException ex) {
                Toast.makeText(SharePlanActivity.this, "Facebook app is not installed.", Toast.LENGTH_SHORT).show();
            }
        });

        // Handle share data through Messenger
        ln3.setOnClickListener(v -> {
            Intent messengerIntent = new Intent(Intent.ACTION_SEND);
            messengerIntent.setType("text/plain");
            messengerIntent.putExtra(Intent.EXTRA_TEXT, shareText);
            messengerIntent.setPackage("com.facebook.orca");
            try {
                startActivity(messengerIntent);
            } catch (android.content.ActivityNotFoundException ex) {
                Toast.makeText(SharePlanActivity.this, "Messenger app is not installed.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
