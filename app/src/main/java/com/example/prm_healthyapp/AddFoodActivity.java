package com.example.prm_healthyapp;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class AddFoodActivity extends AppCompatActivity {

    private EditText editTextName, editTextFat, editTextProtein, editTextCarbohydrates, editTextFiber, editTextVitamins, editTextMinerals;
    private Button buttonSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_food);

        editTextName = findViewById(R.id.editTextFoodName);
        editTextFat = findViewById(R.id.editTextFat);
        editTextProtein = findViewById(R.id.editTextProtein);
        editTextCarbohydrates = findViewById(R.id.editTextCarbohydrates);
        editTextFiber = findViewById(R.id.editTextFiber);
        editTextVitamins = findViewById(R.id.editTextVitamins);
        editTextMinerals = findViewById(R.id.editTextMinerals);
        buttonSave = findViewById(R.id.buttonSave);

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveFood();
            }
        });
    }

    private void saveFood() {
        String name = editTextName.getText().toString();
        double fat = Double.parseDouble(editTextFat.getText().toString());
        double protein = Double.parseDouble(editTextProtein.getText().toString());
        double carbohydrates = Double.parseDouble(editTextCarbohydrates.getText().toString());
        double fiber = Double.parseDouble(editTextFiber.getText().toString());
        String vitamins = editTextVitamins.getText().toString();
        String minerals = editTextMinerals.getText().toString();

        // Inserting data into database
        SQLiteDatabase db = this.openOrCreateDatabase("health_management.db", MODE_PRIVATE, null);
        db.execSQL("INSERT INTO food (name, fat, protein, carbohydrates, fiber, vitamins, minerals) VALUES (?, ?, ?, ?, ?, ?, ?)",
                new Object[]{name, fat, protein, carbohydrates, fiber, vitamins, minerals});
        Toast.makeText(this, "Food saved", Toast.LENGTH_SHORT).show();
    }
}