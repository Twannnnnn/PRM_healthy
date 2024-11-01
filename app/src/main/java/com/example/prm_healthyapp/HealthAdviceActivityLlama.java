package com.example.prm_healthyapp;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HealthAdviceActivityLlama extends AppCompatActivity {
    private TextView textViewResult;
    private Button buttonGetAdvice;
    private Button buttonSendAdviceRequest;
    private EditText editTextInput;
    private DatabaseHelper dbHelper;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_advice_llama);

        textViewResult = findViewById(R.id.textViewResult);
        buttonGetAdvice = findViewById(R.id.buttonGetAdvice);
        buttonSendAdviceRequest = findViewById(R.id.buttonSendAdviceRequest);
        editTextInput = findViewById(R.id.editTextInput);

        // Kiểm tra xem textViewResult có phải là null không
        if (textViewResult != null) {
            String adviceRequest = getIntent().getStringExtra("adviceRequest");
            if (adviceRequest != null) {
                sendAdviceRequest(adviceRequest);
            }
        }

        dbHelper = new DatabaseHelper(this);

        buttonGetAdvice.setOnClickListener(v -> {
            String foodDescription = editTextInput.getText().toString();
            if (!foodDescription.isEmpty()) {
                // Hiển thị câu hỏi của người dùng
                String userMessage = "You: " + foodDescription + "\n\n"; // Thêm một dòng trống
                textViewResult.append(userMessage); // Thêm câu hỏi vào TextView

                YourRequestType request = new YourRequestType(
                        Collections.singletonList(new Message("user", foodDescription)),
                        "llama3-8b-8192"
                );

                GroqApiService apiService = RetrofitClient.getApiService();
                apiService.createChatCompletion(request).enqueue(new Callback<YourResponseType>() {
                    @Override
                    public void onResponse(Call<YourResponseType> call, Response<YourResponseType> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            List<YourResponseType.Choice> choices = response.body().getChoices(); // Tham chiếu đến Choice
                            if (choices != null && !choices.isEmpty()) {
                                StringBuilder responseMessage = new StringBuilder();
                                for (YourResponseType.Choice choice : choices) {
                                    String aiResponse = choice.getMessage().getContent(); // Lấy nội dung từ message
                                    responseMessage.append("AI: ").append(aiResponse).append("\n\n"); // Định dạng phần trả lời của AI và thêm khoảng cách
                                }
                                textViewResult.append(responseMessage.toString()); // Đặt nội dung vào TextView
                            } else {
                                textViewResult.append("AI: No choices found in response.\n\n");
                            }
                        } else {
                            handleErrorResponse(response);
                        }
                    }

                    @Override
                    public void onFailure(Call<YourResponseType> call, Throwable t) {
                        Log.e("API Error", "Failure: " + t.getMessage());
                    }
                });
            } else {
                textViewResult.setText("Please enter a food description.");
            }
        });

        buttonSendAdviceRequest.setOnClickListener(v -> {
            User user = dbHelper.getFirstUser();
            if (user != null) {
                String adviceRequest1 = createAdviceRequest(user);
                sendAdviceRequest(adviceRequest1);
            } else {
                textViewResult.setText("User not found.");
            }
        });
    }

    String createAdviceRequest(User user) {
        StringBuilder requestBuilder = new StringBuilder();
        requestBuilder.append("User Information:\n")
                .append("Name: ").append(user.getName()).append("\n")
                .append("Age: ").append(user.getAge()).append("\n")
                .append("Weight: ").append(user.getWeight()).append("\n")
                .append("Height: ").append(user.getHeight()).append("\n");

        return requestBuilder.toString();
    }

    void sendAdviceRequest(String adviceRequest) {
        YourRequestType request = new YourRequestType(
                Collections.singletonList(new Message("user", adviceRequest)),
                "llama3-8b-8192"
        );

        GroqApiService apiService = RetrofitClient.getApiService();
        apiService.createChatCompletion(request).enqueue(new Callback<YourResponseType>() {
            @Override
            public void onResponse(Call<YourResponseType> call, Response<YourResponseType> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<YourResponseType.Choice> choices = response.body().getChoices(); // Tham chiếu đến Choice
                    if (choices != null && !choices.isEmpty()) {
                        StringBuilder responseMessage = new StringBuilder();
                        for (YourResponseType.Choice choice : choices) {
                            String aiResponse = choice.getMessage().getContent(); // Lấy nội dung từ message
                            responseMessage.append("Professor: ").append(aiResponse).append("\n\n"); // Định dạng phần trả lời của AI và thêm khoảng cách
                            editTextInput.setText(" ");
                        }
                        textViewResult.append(responseMessage.toString()); // Đặt nội dung vào TextView
                    } else {
                        textViewResult.append("AI: No choices found in response.\n\n");
                    }
                } else {
                    textViewResult.append("Failed to retrieve response.\n\n");
                }
            }

            @Override
            public void onFailure(Call<YourResponseType> call, Throwable t) {
                Log.e("API Error", "Failure: " + t.getMessage());
            }
        });
    }

    private void handleErrorResponse(Response<YourResponseType> response) {
        String errorMessage = "Error: " + response.code() + " - " + response.message();
        if (response.errorBody() != null) {
            try {
                String errorBody = response.errorBody().string();
                errorMessage += "\nDetails: " + errorBody;
            } catch (Exception e) {
                e.printStackTrace();
                errorMessage += "\nDetails: Unable to parse error body.";
            }
        }
        Log.e("API Error", errorMessage);
        textViewResult.setText(errorMessage);
    }
}