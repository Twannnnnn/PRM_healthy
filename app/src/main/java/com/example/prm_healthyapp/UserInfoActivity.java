package com.example.prm_healthyapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class UserInfoActivity extends AppCompatActivity {
    private TextView textViewName;
    private TextView textViewEmail;
    private TextView textViewPassword;
    private TextView textViewAge;
    private TextView textViewGender;
    private TextView textViewWeight;
    private TextView textViewHeight;
    private TextView textViewBMI;
    private TextView textViewBodyFat;
    private TextView textViewWaist;
    private TextView textViewNeck;
    private TextView textViewHips;
    private Button buttonUpdateUserInfo;

    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);


        // Khởi tạo DatabaseHelper
        dbHelper = new DatabaseHelper(this);

        // Gọi hàm bindingView để liên kết các View
        bindingView();

        // Gọi hàm bindingAction để lấy dữ liệu người dùng
        bindingAction();
    }

    private void bindingView() {
        textViewName = findViewById(R.id.textViewName);
        textViewEmail = findViewById(R.id.textViewEmail);
        textViewPassword = findViewById(R.id.textViewPassword);
        textViewAge = findViewById(R.id.textViewAge);
        textViewGender = findViewById(R.id.textViewGender);
        textViewWeight = findViewById(R.id.textViewWeight);
        textViewHeight = findViewById(R.id.textViewHeight);
        textViewBMI = findViewById(R.id.textViewBMI);
        textViewBodyFat = findViewById(R.id.textViewBodyFat);
        textViewWaist = findViewById(R.id.textViewWaist);
        textViewNeck = findViewById(R.id.textViewNeck);
        textViewHips = findViewById(R.id.textViewHips);
        buttonUpdateUserInfo = findViewById(R.id.buttonUpdateUserInfo);
    }

    private void bindingAction() {
        User user = dbHelper.getFirstUser();
        if (user != null) {
            textViewName.setText("Name: " + user.getName());
            textViewEmail.setText("Email: " + user.getEmail());
            textViewPassword.setText("Password: " + user.getPassword());
            textViewAge.setText("Age: " + user.getAge());
            textViewGender.setText("Gender: " + user.getGender());
            textViewWeight.setText("Weight: " + user.getWeight());
            textViewHeight.setText("Height: " + user.getHeight());
            textViewBMI.setText("BMI: " + user.getBmi());
            textViewBodyFat.setText("Body Fat Percentage: " + user.getBodyFatPercentage());
            textViewWaist.setText("Waist: " + user.getWaist());
            textViewNeck.setText("Neck: " + user.getNeck());
            textViewHips.setText("Hips: " + user.getHips());
        } else {
            textViewName.setText("No user found.");
            textViewEmail.setText("");
            textViewPassword.setText("");
            textViewAge.setText("");
            textViewGender.setText("");
            textViewWeight.setText("");
            textViewHeight.setText("");
            textViewBMI.setText("");
            textViewBodyFat.setText("");
            textViewWaist.setText("");
            textViewNeck.setText("");
            textViewHips.setText("");
        }
        buttonUpdateUserInfo.setOnClickListener(v -> {
            Intent intent = new Intent(UserInfoActivity.this, UpdateUserInformationActivity.class);
            startActivity(intent);
        });
    }
}