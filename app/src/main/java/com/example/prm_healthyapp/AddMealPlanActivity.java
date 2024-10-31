package com.example.prm_healthyapp;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Calendar;
import java.util.List;

public class AddMealPlanActivity extends AppCompatActivity {

    private LinearLayout foodCheckboxContainer;
    private Button buttonSaveMealPlan;
    private DatabaseHelper dbHelper;
    private EditText editTextPlanDate;
    private EditText editTextCalories;
    private Spinner spinnerMealType;
    private double totalCalories = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_meal_plan);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.addMealPlan), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        foodCheckboxContainer = findViewById(R.id.foodCheckboxContainer);
        buttonSaveMealPlan = findViewById(R.id.buttonSaveMealPlan);
        dbHelper = new DatabaseHelper(this);
        editTextPlanDate = findViewById(R.id.editTextPlanDate);
        editTextCalories = findViewById(R.id.editTextCalories);
        spinnerMealType = findViewById(R.id.spinnerMealType);

        editTextPlanDate.setOnClickListener(v -> showDatePickerDialog());

        // Tải danh sách món ăn và tạo checkbox
        loadFoodCheckboxes();

        // Xử lý nút Lưu
        buttonSaveMealPlan.setOnClickListener(v -> saveMealPlan());
    }

    private void showDatePickerDialog() {
        // Lấy ngày hiện tại
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // Tạo DatePickerDialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, selectedYear, selectedMonth, selectedDay) -> {
            // Cập nhật EditText với ngày đã chọn
            String selectedDate = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear; // Tháng bắt đầu từ 0
            editTextPlanDate.setText(selectedDate);

            // Kiểm tra nếu ngày chọn là trong quá khứ
            if (!isDateInFuture(selectedYear, selectedMonth, selectedDay)) {
                showErrorDialog("Ngaỳ không hợp hệ ", "Hãy chọn ngày lập kế hoạch ở tương lai");
                editTextPlanDate.setText(""); // Xóa ngày đã chọn
            }

        }, year, month, day);

        // Hiển thị DatePickerDialog
        datePickerDialog.show();
    }


    private boolean isDateInFuture(int year, int month, int day) {
        Calendar selectedDate = Calendar.getInstance();
        selectedDate.set(year, month, day);

        Calendar today = Calendar.getInstance();
        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);
        today.set(Calendar.MILLISECOND, 0);

        return selectedDate.after(today); // Kiểm tra xem ngày chọn có sau ngày hiện tại không
    }

    private void showErrorDialog(String title, String message) {
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", null)
                .show();
    }


    private void loadFoodCheckboxes() {
        foodCheckboxContainer.removeAllViews(); // Xóa các checkbox cũ
        List<FoodItem> foodItems = dbHelper.getAllFoodItems(); // Lấy danh sách món ăn


        for (FoodItem food : foodItems) {
            CheckBox checkBox = new CheckBox(this);

            // Lấy tên và lượng calo để hiển thị
            String displayText = food.getName() + " - " + food.getTotal_calories() + " calo"; // Tạo chuỗi hiển thị
            checkBox.setText(displayText); // Đặt chuỗi hiển thị cho checkbox

            // Thiết lập sự kiện lắng nghe thay đổi trạng thái checkbox
            checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                // Cập nhật tổng lượng calo khi checkbox được chọn hoặc bỏ chọn
                if (isChecked) {
                    totalCalories += food.getTotal_calories(); // Cộng thêm lượng calo
                } else {
                    totalCalories -= food.getTotal_calories(); // Trừ đi lượng calo
                }
                // Cập nhật vào EditText
                editTextCalories.setText(String.valueOf(totalCalories)); // Hiển thị tổng lượng calo
            });

            foodCheckboxContainer.addView(checkBox); // Thêm checkbox vào container
        }
    }

    private void saveMealPlan() {
        StringBuilder selectedFoods = new StringBuilder();

        String selectedDate = editTextPlanDate.getText().toString();
        if (selectedDate.isEmpty()) {
            Toast.makeText(this, "Vui lòng chọn ngày kế hoạch.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Duyệt qua tất cả checkbox và lưu những món đã chọn
        for (int i = 0; i < foodCheckboxContainer.getChildCount(); i++) {
            CheckBox checkBox = (CheckBox) foodCheckboxContainer.getChildAt(i);
            if (checkBox.isChecked()) {
                selectedFoods.append(checkBox.getText().toString()).append(", ");
            }
        }

        // Loại bỏ dấu phẩy cuối cùng
        if (selectedFoods.length() > 0) {
            selectedFoods.setLength(selectedFoods.length() - 2);
        }

        String mealType = spinnerMealType.getSelectedItem().toString();
        String caloriesStr = editTextCalories.getText().toString();
        String planDate = editTextPlanDate.getText().toString();

        if (caloriesStr.isEmpty() || planDate.isEmpty()) {
            showErrorDialog("Thông tin không đầy đủ", "Vui lòng điền đủ thông tin.");
            return;
        }

        double calories = Double.parseDouble(caloriesStr);
        int userId = 1; // Giả sử đã có phương thức lấy User ID

        if (dbHelper.checkMealPlanExists(userId, mealType, planDate)) {
            showErrorDialog("Kế hoạch đã tồn tại", "Kế hoạch bữa ăn cho " + mealType + " này đã tồn tại vào ngày " + planDate + ".");
            return;
        }


        // Lưu vào database
        boolean isInserted = dbHelper.insertMealPlan(userId, mealType, selectedFoods.toString(), calories, planDate);

        if (isInserted) {
            Toast.makeText(this, "Lập kế hoạch thành công.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Lỗi khi lưu kế hoạch bữa ăn.", Toast.LENGTH_SHORT).show();
        }
    }
}