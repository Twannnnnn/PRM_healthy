package com.example.prm_healthyapp;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private Button btnGoToReport, btnAddFood;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnGoToReport = findViewById(R.id.btnGoToReport);
        btnAddFood = findViewById(R.id.btnAddFood);

        btnAddFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddFoodDialog();
            }
        });
    }

    private void showAddFoodDialog() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_add_food);
        dialog.setCancelable(true);

        EditText editTextFoodName = dialog.findViewById(R.id.editTextFoodName);
        EditText editTextFat = dialog.findViewById(R.id.editTextFat);
        EditText editTextProtein = dialog.findViewById(R.id.editTextProtein);
        EditText editTextCarbohydrates = dialog.findViewById(R.id.editTextCarbohydrates);
        EditText editTextFiber = dialog.findViewById(R.id.editTextFiber);
        Button buttonSaveFood = dialog.findViewById(R.id.buttonSaveFood);

        buttonSaveFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editTextFoodName.getText().toString();
                float fat = Float.parseFloat(editTextFat.getText().toString());
                float protein = Float.parseFloat(editTextProtein.getText().toString());
                float carbohydrates = Float.parseFloat(editTextCarbohydrates.getText().toString());
                float fiber = Float.parseFloat(editTextFiber.getText().toString());

                // Gọi phương thức để lưu thực phẩm vào cơ sở dữ liệu
                // dbHelper.addFood(name, fat, protein, carbohydrates, fiber, vitamins, minerals);

                Toast.makeText(MainActivity.this, "Thực phẩm đã được thêm", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

        dialog.show();
    }
}