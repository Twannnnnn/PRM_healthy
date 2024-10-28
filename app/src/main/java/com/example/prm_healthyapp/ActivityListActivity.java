package com.example.prm_healthyapp;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ActivityListActivity extends AppCompatActivity implements ActivityAdapter.OnActivityListener {

    private Button btnAddActivity;
    private RecyclerView recyclerViewActivities;
    private ActivityAdapter activityAdapter;
    private DatabaseHelper dbHelper;
    private List<ActivityModel> activityList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_list);
        dbHelper = new DatabaseHelper(this);

        bindingView();
        bindingAction();
    }

    private void bindingView() {
        btnAddActivity = findViewById(R.id.btnAddActivity);
        recyclerViewActivities = findViewById(R.id.recyclerViewActivities);
        recyclerViewActivities.setLayoutManager(new LinearLayoutManager(this));
        activityList = new ArrayList<>();
        activityAdapter = new ActivityAdapter(activityList, this);
        recyclerViewActivities.setAdapter(activityAdapter);
    }

    private void bindingAction() {
        btnAddActivity.setOnClickListener(view -> {
            Intent intent = new Intent(ActivityListActivity.this, AddUpdateActivity.class);
            startActivity(intent);
        });

        loadActivities();
    }

    private void loadActivities() {
        activityList.clear();
        Cursor cursor = dbHelper.getAllActivities();

        // Store column indices in variables and check for -1 values
        int idIndex = cursor.getColumnIndex("id");
        int userIdIndex = cursor.getColumnIndex("user_id");
        int nameIndex = cursor.getColumnIndex("name");
        int descriptionIndex = cursor.getColumnIndex("description");
        int startTimeIndex = cursor.getColumnIndex("start_time");
        int endTimeIndex = cursor.getColumnIndex("end_time");
        int reminderIndex = cursor.getColumnIndex("reminder");

        while (cursor.moveToNext()) {
            ActivityModel activity = new ActivityModel(
                    idIndex >= 0 ? cursor.getInt(idIndex) : 0,
                    userIdIndex >= 0 ? cursor.getInt(userIdIndex) : 0,
                    nameIndex >= 0 ? cursor.getString(nameIndex) : "",
                    descriptionIndex >= 0 ? cursor.getString(descriptionIndex) : "",
                    startTimeIndex >= 0 ? cursor.getString(startTimeIndex) : "",
                    endTimeIndex >= 0 ? cursor.getString(endTimeIndex) : "",
                    reminderIndex >= 0 && cursor.getInt(reminderIndex) == 1
            );
            activityList.add(activity);
        }
        cursor.close();
        activityAdapter.notifyDataSetChanged();
    }



    @Override
    public void onDeleteClick(int position) {
        ActivityModel activity = activityList.get(position);
        dbHelper.deleteActivity(activity.getId());
        loadActivities();
    }

    @Override
    public void onUpdateClick(int position) {
        ActivityModel activity = activityList.get(position);
        Intent intent = new Intent(ActivityListActivity.this, AddUpdateActivity.class);
        intent.putExtra("activityId", activity.getId());
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadActivities();
    }
}