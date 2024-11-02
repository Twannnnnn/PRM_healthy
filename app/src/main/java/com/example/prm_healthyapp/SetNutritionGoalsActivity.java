package com.example.prm_healthyapp;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class SetNutritionGoalsActivity extends AppCompatActivity {

    private EditText calorieGoalEditText;
    private EditText proteinGoalEditText;
    private EditText fatGoalEditText;
    private TextView targetDateTextView;
    private TextView feedbackTextView;
    private TextView currentWeightTextView;
    private TextView currentHeightTextView;
    private TextView bmiEvaluationTextView;
    private DatabaseHelper mealPlanDatabase;
    private Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_nutrition_goals);

        DatabaseHelper dbHelper = new DatabaseHelper(this);
        User user = dbHelper.getFirstUser(); // Lấy thông tin người dùng từ database

        currentWeightTextView = findViewById(R.id.currentWeightTextView);
        currentHeightTextView = findViewById(R.id.currentHeightTextView);
        bmiEvaluationTextView = findViewById(R.id.bmiEvaluationTextView);

        // Khởi tạo các EditText
        calorieGoalEditText = findViewById(R.id.calorieGoalEditText);
        proteinGoalEditText = findViewById(R.id.proteinGoalEditText);
        fatGoalEditText = findViewById(R.id.fatGoalEditText);
        targetDateTextView = findViewById(R.id.targetDateTextView);
        feedbackTextView = findViewById(R.id.feedbackTextView);

        Button saveGoalsButton = findViewById(R.id.saveGoalsButton);

        if (user != null) {
            double currentWeight = user.getWeight();
            double currentHeight = user.getHeight(); // Chiều cao tính bằng cm
            int age = user.getAge();
            String gender = user.getGender();

            currentWeightTextView.setText("Cân nặng hiện tại: " + currentWeight + " kg");
            currentHeightTextView.setText("Chiều cao hiện tại: " + currentHeight + " cm");

            // Tính BMI
            double heightInMeters = currentHeight / 100; // Chuyển chiều cao sang mét
            double bmi = currentWeight / (heightInMeters * heightInMeters);

            // Đánh giá tình trạng và tính mục tiêu dinh dưỡng
            String bmiEvaluation;
            double dailyCaloricNeeds;
            double proteinGoal;
            double fatGoal;

            if (bmi < 18.5) {
                bmiEvaluation = "Gầy";
                dailyCaloricNeeds = calculateCaloricNeeds(currentWeight, currentHeight, age, gender) + 500; // Tăng 500 kcal/ngày
                proteinGoal = 1.5 * currentWeight; // 1.5g protein/kg
                fatGoal = 0.8 * currentWeight; // 0.8g fat/kg
            } else if (bmi < 25) {
                bmiEvaluation = "Cân đối";
                dailyCaloricNeeds = calculateCaloricNeeds(currentWeight, currentHeight, age, gender); // Giữ nguyên
                proteinGoal = 1.0 * currentWeight; // 1g protein/kg
                fatGoal = 0.7 * currentWeight; // 0.7g fat/kg
            } else if (bmi < 30) {
                bmiEvaluation = "Thừa cân";
                dailyCaloricNeeds = calculateCaloricNeeds(currentWeight, currentHeight, age, gender) - 500; // Giảm 500 kcal/ngày
                proteinGoal = 0.8 * currentWeight; // 0.8g protein/kg
                fatGoal = 0.5 * currentWeight; // 0.5g fat/kg
            } else {
                bmiEvaluation = "Béo phì";
                dailyCaloricNeeds = calculateCaloricNeeds(currentWeight, currentHeight, age, gender) - 700; // Giảm 700 kcal/ngày
                proteinGoal = 0.8 * currentWeight; // 0.8g protein/kg
                fatGoal = 0.5 * currentWeight; // 0.5g fat/kg
            }

            // Đặt mục tiêu vào EditText
            calorieGoalEditText.setText(String.format("Lượng calo cần đạt: %.2f kcal", dailyCaloricNeeds));
            proteinGoalEditText.setText("Lượng protein cần đạt: " + proteinGoal + " g");
            fatGoalEditText.setText("Lượng chất béo cần đạt: " + fatGoal + " g");
            bmiEvaluationTextView.setText("Đánh giá BMI: " + bmiEvaluation);

            int daysToReachGoal = 0;
            double dailyCaloricAdjustment = dailyCaloricNeeds - calculateCaloricNeeds(currentWeight, currentHeight, age, gender); // This will be positive if we need a surplus, negative if we need a deficit

            // Tính trọng lượng tối ưu cho chỉ số BMI "Cân đối"
            double optimalWeight = 22.0 * (heightInMeters * heightInMeters); // Giả sử mục tiêu là BMI 22
            double weightChangeGoal = optimalWeight - currentWeight; // Mục tiêu thay đổi trọng lượng
            double totalCaloricChange = weightChangeGoal * 7700; // kcal
            if (dailyCaloricAdjustment != 0) {
                daysToReachGoal = (int) Math.abs(totalCaloricChange / dailyCaloricAdjustment); // Days needed to reach goal
                Calendar targetCalendar = Calendar.getInstance();
                targetCalendar.add(Calendar.DAY_OF_MONTH, daysToReachGoal);

                // Format the completion date
                String estimatedCompletionDate = targetCalendar.get(Calendar.DAY_OF_MONTH) + "/" +
                        (targetCalendar.get(Calendar.MONTH) + 1) + "/" + targetCalendar.get(Calendar.YEAR);
                targetDateTextView.setText("Ngày hoàn thành dự kiến: " + estimatedCompletionDate);
            } else {
                targetDateTextView.setText("Không có thay đổi về calo cần thiết để đạt mục tiêu cân nặng.");
            }

            Calendar targetCalendar = Calendar.getInstance();
            targetCalendar.add(Calendar.DAY_OF_MONTH, daysToReachGoal);
            String estimatedCompletionDate = targetCalendar.get(Calendar.DAY_OF_MONTH) + "/" +
                    (targetCalendar.get(Calendar.MONTH) + 1) + "/" + targetCalendar.get(Calendar.YEAR);
            targetDateTextView.setText("Ngày hoàn thành dự kiến: " + estimatedCompletionDate);

            saveGoalsButton.setOnClickListener(view -> {
                String calorieGoal = calorieGoalEditText.getText().toString();
                String proteinGoalStr = proteinGoalEditText.getText().toString();
                String fatGoalStr = fatGoalEditText.getText().toString();
                String setDate = targetDateTextView.getText().toString();

                // Kiểm tra xem các trường có giá trị hay không
                if (calorieGoal.isEmpty() || proteinGoalStr.isEmpty() || fatGoalStr.isEmpty()) {
                    feedbackTextView.setText("Vui lòng nhập tất cả các mục tiêu dinh dưỡng.");
                    feedbackTextView.setVisibility(View.VISIBLE);
                    return;
                }

                // Lưu vào database
                dbHelper.saveNutritionGoals(dbHelper.getFirstUser().getId(), calorieGoal, proteinGoalStr, fatGoalStr, setDate);
                feedbackTextView.setText("Mục tiêu dinh dưỡng đã được lưu thành công!");
                feedbackTextView.setVisibility(View.VISIBLE);
            });

        } else {
            currentWeightTextView.setText("Không tìm thấy thông tin người dùng.");
            currentHeightTextView.setText("");
            bmiEvaluationTextView.setText("");
        }
    }

    private double calculateCaloricNeeds(double weight, double height, int age, String gender) {
        // Sử dụng công thức Harris-Benedict để tính nhu cầu calo hàng ngày
        if (gender.equalsIgnoreCase("male")) {
            return 88.362 + (13.397 * weight) + (4.799 * height) - (5.677 * age); // Đối với nam
        } else {
            return 447.593 + (9.247 * weight) + (3.098 * height) - (4.330 * age); // Đối với nữ
        }
    }
}