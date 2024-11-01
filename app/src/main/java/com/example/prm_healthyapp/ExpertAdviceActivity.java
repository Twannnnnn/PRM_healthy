package com.example.prm_healthyapp;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class ExpertAdviceActivity extends AppCompatActivity {

    private LinearLayout lnNutrition, lnHealth, lnOther;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expert_advice);

        // Initialize the views
        lnNutrition = findViewById(R.id.ln_nutrition);
        lnHealth = findViewById(R.id.ln_health);
        lnOther = findViewById(R.id.ln_other);

        // Set click listeners for each section
        lnNutrition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNutritionDialog();
            }
        });

        lnHealth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showHealthDialog();
            }
        });

        lnOther.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showOtherDialog();
            }
        });
    }

    // Show dialog for Nutrition section
    private void showNutritionDialog() {
        String[] nutrients = {"Fat", "Protein", "Carbohydrate"};

        new AlertDialog.Builder(this)
                .setTitle("Nutrition")
                .setItems(nutrients, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String nutrient = nutrients[which];
                        showDetailsDialog(nutrient);
                    }
                })
                .setNegativeButton("Close", null)
                .show();
    }

    // Show dialog for Health section with detailed topics
    private void showHealthDialog() {
        String[] healthTopics = {"Exercise Guidelines", "Mental Health Tips", "Sleep Recommendations"};

        new AlertDialog.Builder(this)
                .setTitle("Health")
                .setItems(healthTopics, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String topic = healthTopics[which];
                        showHealthTopicDialog(topic);
                    }
                })
                .setNegativeButton("Close", null)
                .show();
    }

    // Show detailed health topic dialog
    private void showHealthTopicDialog(String topic) {
        String message;
        switch (topic) {
            case "Exercise Guidelines":
                message = "Adults should aim for at least 150 minutes of moderate aerobic exercise per week. Examples include brisk walking, cycling, or swimming.";
                break;
            case "Mental Health Tips":
                message = "Engage in mindfulness practices, stay connected with loved ones, and make time for hobbies to maintain good mental health.";
                break;
            case "Sleep Recommendations":
                message = "Adults should aim for 7-9 hours of sleep per night. Maintaining a regular sleep schedule and avoiding screens before bed can help improve sleep quality.";
                break;
            default:
                message = "No information available.";
        }

        new AlertDialog.Builder(this)
                .setTitle(topic)
                .setMessage(message)
                .setPositiveButton("OK", null)
                .show();
    }

    // Show dialog for Other section with FAQs
    private void showOtherDialog() {
        String[] faqs = {
                "How many kilometers should I run each week?",
                "What time is best for exercise?",
                "How much water should I drink daily?",
                "Should I take vitamin supplements?",
                "Is it okay to skip breakfast?"
        };

        new AlertDialog.Builder(this)
                .setTitle("FAQs")
                .setItems(faqs, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String question = faqs[which];
                        showFaqAnswerDialog(question);
                    }
                })
                .setNegativeButton("Close", null)
                .show();
    }

    // Show dialog with answers to FAQs
    private void showFaqAnswerDialog(String question) {
        String answer;
        switch (question) {
            case "How many kilometers should I run each week?":
                answer = "It depends on your fitness level, but a common goal is 20-30 km per week for fitness enthusiasts. Beginners might start with 10-15 km per week.";
                break;
            case "What time is best for exercise?":
                answer = "Any time that suits your schedule is fine. Morning workouts can boost energy for the day, while evening workouts may help with relaxation.";
                break;
            case "How much water should I drink daily?":
                answer = "On average, men need about 3.7 liters, and women need about 2.7 liters of fluids per day, including from food and beverages.";
                break;
            case "Should I take vitamin supplements?":
                answer = "If you eat a balanced diet, you might not need supplements. Consult a healthcare provider before starting any supplements.";
                break;
            case "Is it okay to skip breakfast?":
                answer = "Skipping breakfast is okay if you maintain a balanced diet throughout the day. Listen to your body and eat when hungry.";
                break;
            default:
                answer = "No answer available.";
        }

        new AlertDialog.Builder(this)
                .setTitle(question)
                .setMessage(answer)
                .setPositiveButton("OK", null)
                .show();
    }

    // Show detailed dialog with nutrient information
    private void showDetailsDialog(String nutrient) {
        String message;
        switch (nutrient) {
            case "Fat":
                message = "Amount: 20-35% of daily calories\nBenefits: Provides energy\nDrawbacks: Can lead to obesity if consumed in excess";
                break;
            case "Protein":
                message = "Amount: 10-35% of daily calories\nBenefits: Builds muscle\nDrawbacks: Can strain kidneys in high amounts";
                break;
            case "Carbohydrate":
                message = "Amount: 45-65% of daily calories\nBenefits: Main source of energy\nDrawbacks: Can lead to weight gain if consumed in excess";
                break;
            default:
                message = "No information available.";
        }

        new AlertDialog.Builder(this)
                .setTitle(nutrient)
                .setMessage(message)
                .setPositiveButton("OK", null)
                .show();
    }
}
