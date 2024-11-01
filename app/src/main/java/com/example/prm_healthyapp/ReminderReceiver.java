package com.example.prm_healthyapp;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import androidx.core.app.NotificationCompat;

public class ReminderReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        int activityId = intent.getIntExtra("activityId", -1);

        // Get the activity name from the database
        DatabaseHelper dbHelper = new DatabaseHelper(context);
        String activityName = dbHelper.getActivityById(activityId).getName();

        if ("Sleep".equals(activityName)) {
            // Create a pending intent to open your main activity
            Intent notificationIntent = new Intent(context, MainActivity.class);
            notificationIntent.putExtra("activityId", activityId);
            notificationIntent.putExtra("action", "start_sleep_log"); // Action for creating sleep log
            PendingIntent pendingIntent = PendingIntent.getActivity(context, activityId, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

            // Create the notification
            String channelId = "your_channel_id"; // Same as used in your channel creation
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channelId)
                    .setSmallIcon(R.drawable.ic_launcher_foreground) // Use your app's icon
                    .setContentTitle("Reminder")
                    .setContentText("It's time to go to bed!") // Notification content
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true); // Dismiss the notification when clicked

            // Show the notification
            NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            if (manager != null) {
                manager.notify(activityId, builder.build());
            }
        }else {

            // Create a pending intent to open your main ac tivity or another relevant activity
            Intent notificationIntent = new Intent(context, MainActivity.class); // Change to your target activity
            notificationIntent.putExtra("activityId", activityId); // Pass any necessary data
            PendingIntent pendingIntent = PendingIntent.getActivity(context, activityId, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

            // Create the notification
            String channelId = "your_channel_id"; // Same as used in your channel creation
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channelId)
                    .setSmallIcon(R.drawable.ic_launcher_foreground) // Use your app's icon
                    .setContentTitle("Reminder")
                    .setContentText("It's time for your activity!")
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true); // Dismiss the notification when clicked

            // Show the notification
            NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            if (manager != null) {
                manager.notify(activityId, builder.build());
            }
        }
    }
}