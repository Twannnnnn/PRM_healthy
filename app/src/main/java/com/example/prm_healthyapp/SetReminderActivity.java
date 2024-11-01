package com.example.prm_healthyapp;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import java.time.Instant;
import java.util.Calendar;

public class SetReminderActivity extends AppCompatActivity {

    private TimePicker timePicker;
    private Button btnSetReminder, btnDeleteReminder;
    private DatabaseHelper dbHelper;
    private int userId; // Assuming you have userId from somewhere
    private int activityId; // Activity ID
    private Reminder existingReminder; // Existing reminder

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_reminder);

        dbHelper = new DatabaseHelper(this);
        timePicker = findViewById(R.id.timePicker);
        btnSetReminder = findViewById(R.id.btnSetReminder);
        btnDeleteReminder = findViewById(R.id.btnDeleteReminder);

        activityId = getIntent().getIntExtra("activityId", -1);

        existingReminder = dbHelper.getReminderForActivity(activityId, userId);
        if (existingReminder != null) {
            String[] timeParts = existingReminder.getReminderTime().split(":");
            timePicker.setHour(Integer.parseInt(timeParts[0]));
            timePicker.setMinute(Integer.parseInt(timeParts[1]));
            btnDeleteReminder.setVisibility(View.VISIBLE);
        } else {
            btnDeleteReminder.setVisibility(View.GONE);
        }

        createNotificationChannel();
        bindingAction();
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = "your_channel_id";
            String channelName = "Reminder Notifications";
            NotificationChannel channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH);
            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(channel);
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void sendNotification(String data) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("notification_data", data);
        PendingIntent pi = PendingIntent.getActivity(this, 10, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        Notification notification = new NotificationCompat.Builder(this, "your_channel_id")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("Notification Title")
                .setContentText(data)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(data))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pi)
                .setAutoCancel(true)
                .build();

        int notificationId = 1; // Use a static ID or implement a counter
        notificationManager.notify(notificationId, notification);
    }

    private void bindingAction() {
        btnSetReminder.setOnClickListener(view -> {
            String reminderTime = String.format("%02d:%02d", timePicker.getHour(), timePicker.getMinute());

            if (existingReminder != null) {
                dbHelper.updateReminder(existingReminder.getId(), reminderTime);
                Toast.makeText(this, "Reminder updated successfully!", Toast.LENGTH_SHORT).show();
            } else {
                dbHelper.addReminder(userId, activityId, reminderTime);
                Toast.makeText(this, "Reminder set successfully!", Toast.LENGTH_SHORT).show();
            }

            requestExactAlarmPermission(reminderTime);
            finish();
        });

        btnDeleteReminder.setOnClickListener(view -> {
            if (existingReminder != null) {
                dbHelper.deleteReminder(existingReminder.getId());
                Toast.makeText(this, "Reminder deleted successfully!", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private void requestExactAlarmPermission(String reminderTime) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (alarmManager != null && !alarmManager.canScheduleExactAlarms()) {
                Intent intent = new Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM);
                startActivity(intent);
            } else {
                setNotification(reminderTime);
            }
        } else {
            setNotification(reminderTime);
        }
    }

    private void setNotification(String reminderTime) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        if (alarmManager != null) {
            Log.d("SetReminderActivity", "Alarm set for: " + reminderTime);
            scheduleExactAlarm(reminderTime, alarmManager);
        }
    }

    private void scheduleExactAlarm(String reminderTime, AlarmManager alarmManager) {
        String[] timeParts = reminderTime.split(":");
        int hour = Integer.parseInt(timeParts[0]);
        int minute = Integer.parseInt(timeParts[1]);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);

        // Adjust for past times
        if (calendar.getTimeInMillis() < System.currentTimeMillis()) {
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }

        Intent intent = new Intent(this, ReminderReceiver.class);
        intent.putExtra("activityId", activityId);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, activityId, intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        // Set a repeating alarm
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            alarmManager.setInexactRepeating(
                    AlarmManager.RTC_WAKEUP,
                    calendar.getTimeInMillis(),
                    AlarmManager.INTERVAL_DAY, // Repeat every day
                    pendingIntent);
        } else {
            alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        }

        Log.d("SetReminderActivity", "Daily alarm scheduled for: " + calendar.getTime());
    }
}