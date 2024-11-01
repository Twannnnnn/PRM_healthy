package com.example.prm_healthyapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.FitnessOptions;
import com.google.android.gms.fitness.data.DataPoint;
import com.google.android.gms.fitness.data.DataSet;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.data.Field;
import com.google.android.gms.fitness.request.DataReadRequest;

import java.util.concurrent.TimeUnit;

public class SyncActivity extends AppCompatActivity {

    private static final String PREFS_NAME = "SyncPrefs";
    private static final String PREF_SYNCED = "isSynced";

    private static final int GOOGLE_FIT_PERMISSIONS_REQUEST_CODE = 1;
    private static final int ACTIVITY_RECOGNITION_PERMISSION_REQUEST_CODE = 100;
    private static final long SYNC_INTERVAL = 1 * 60 * 1000; // 5 minutes in milliseconds

    private final Handler handler = new Handler();
    private final Runnable syncRunnable = new Runnable() {
        @Override
        public void run() {
            requestGoogleFitPermissions();
            handler.postDelayed(this, SYNC_INTERVAL); // Re-run this every SYNC_INTERVAL
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sync);

        Button btnSync = findViewById(R.id.btnSync);
        TextView tvDistance = findViewById(R.id.tvDistance);
        TextView tvStep = findViewById(R.id.tvStep);
        ImageButton btnBack = findViewById(R.id.btnBack);

        // Check if data has already been synced
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        boolean isSynced = prefs.getBoolean(PREF_SYNCED, false);

        if (isSynced) {
            // Hide the sync button and show the data
            btnSync.setVisibility(View.GONE);
            tvDistance.setVisibility(View.VISIBLE);
            tvStep.setVisibility(View.VISIBLE);
            loadDataAndDisplay();
        } else {
            // Show the sync button and hide the data view
            btnSync.setVisibility(View.VISIBLE);
            tvDistance.setVisibility(View.GONE);
            tvStep.setVisibility(View.GONE);

            btnSync.setOnClickListener(v -> {
                requestGoogleFitPermissions();
            });
        }

        btnBack.setOnClickListener(v -> onBackPressed());
    }

    @Override
    protected void onStart() {
        super.onStart();
        handler.post(syncRunnable); // Start the sync task
    }

    @Override
    protected void onStop() {
        super.onStop();
        handler.removeCallbacks(syncRunnable); // Stop the sync task
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == ACTIVITY_RECOGNITION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, proceed to sync
                requestGoogleFitPermissions();
            } else {
                Toast.makeText(this, "Activity recognition permission is required to access step count data.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void requestGoogleFitPermissions() {
        // Define the fitness options with required data types
        FitnessOptions fitnessOptions = FitnessOptions.builder()
                .addDataType(DataType.TYPE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_READ)
                .addDataType(DataType.TYPE_DISTANCE_DELTA, FitnessOptions.ACCESS_READ)
                .build();

        // Get the currently signed-in Google account
        GoogleSignInAccount account = GoogleSignIn.getAccountForExtension(this, fitnessOptions);

        // Check if permissions are granted
        if (!GoogleSignIn.hasPermissions(account, fitnessOptions)) {
            // Request permissions if not granted
            GoogleSignIn.requestPermissions(
                    this,
                    GOOGLE_FIT_PERMISSIONS_REQUEST_CODE,
                    account,
                    fitnessOptions);
        } else {
            // Access Google Fit data directly if permissions are already granted
            accessGoogleFitData(account);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GOOGLE_FIT_PERMISSIONS_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                GoogleSignInAccount account = GoogleSignIn.getAccountForExtension(this, getFitnessOptions());
                accessGoogleFitData(account);
            } else {
                Log.e("SyncActivity", "Google Fit permissions were denied.");
            }
        }
    }

    private FitnessOptions getFitnessOptions() {
        return FitnessOptions.builder()
                .addDataType(DataType.TYPE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_READ)
                .addDataType(DataType.TYPE_DISTANCE_DELTA, FitnessOptions.ACCESS_READ)
                .build();
    }

    private void loadDataAndDisplay() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String syncedData = prefs.getString("syncedData", "No data available.");

        TextView tvDistance = findViewById(R.id.tvDistance);
        tvDistance.setText(syncedData);
        TextView tvStep = findViewById(R.id.tvStep);
        tvStep.setText(syncedData);
    }


    private void accessGoogleFitData(GoogleSignInAccount account) {
        // Set the time range for the query (last 24 hours)
        long endTime = System.currentTimeMillis();
        long startTime = endTime - TimeUnit.DAYS.toMillis(1); // 24 hours ago

        // Create a query for both step count and distance data in one request
        DataReadRequest readRequest = new DataReadRequest.Builder()
                .aggregate(DataType.TYPE_STEP_COUNT_DELTA)
                .aggregate(DataType.TYPE_DISTANCE_DELTA)
                .setTimeRange(startTime, endTime, TimeUnit.MILLISECONDS)
                .bucketByTime(1, TimeUnit.DAYS) // Set bucket to daily intervals
                .build();

        // Read both step count and distance data
        Fitness.getHistoryClient(this, account)
                .readData(readRequest)
                .addOnSuccessListener(dataReadResponse -> {
                    int totalSteps = 0;
                    float totalDistance = 0f;

                    // Iterate over each bucket
                    for (DataSet dataSet : dataReadResponse.getDataSets()) {
                        for (DataPoint dataPoint : dataSet.getDataPoints()) {
                            // Extract step count if available
                            if (dataPoint.getDataType().equals(DataType.TYPE_STEP_COUNT_DELTA)) {
                                totalSteps += dataPoint.getValue(Field.FIELD_STEPS).asInt();
                            }
                            // Extract distance if available
                            if (dataPoint.getDataType().equals(DataType.TYPE_DISTANCE_DELTA)) {
                                totalDistance += dataPoint.getValue(Field.FIELD_DISTANCE).asFloat();
                            }
                        }
                    }

                    // Prepare strings for displaying data
                    String stepData = "Total Steps: " + totalSteps;
                    String distanceData = "Total Distance: " + totalDistance + " meters";

                    // Log fetched data
                    Log.d("SyncActivity", "Fetched step count data successfully: " + totalSteps);
                    Log.d("SyncActivity", "Fetched distance data successfully: " + totalDistance + " meters");

                    // Save and display data
                    saveDataAndDisplay(distanceData, stepData);
                })
                .addOnFailureListener(e -> Log.e("SyncActivity", "Failed to read Google Fit data.", e));
    }

    private void saveDataAndDisplay(String distance, String step) {
        SharedPreferences.Editor editor = getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit();
        editor.putBoolean(PREF_SYNCED, true);
        editor.putString("syncedDistance", distance);
        editor.putString("syncedStep", step);
        editor.apply();

        Button btnSync = findViewById(R.id.btnSync);
        TextView tvDistance = findViewById(R.id.tvDistance);
        TextView tvStep = findViewById(R.id.tvStep);

        btnSync.setVisibility(View.GONE);
        tvDistance.setVisibility(View.VISIBLE);
        tvDistance.setText(distance);
        tvStep.setVisibility(View.VISIBLE);
        tvStep.setText(step);
    }


}
