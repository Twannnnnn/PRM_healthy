package com.example.prm_healthyapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            updateSleepLogEndTime(context);
        }
    }

    private void updateSleepLogEndTime(Context context) {
        DatabaseHelper dbHelper = new DatabaseHelper(context);
        SleepLogModel lastSleepLog = dbHelper.getLastSleepLog();

        if (lastSleepLog != null && lastSleepLog.getSleepEnd() == null) { // Only update if end time is not set
            String sleepEndTime = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());
            dbHelper.updateSleepLog(lastSleepLog.getId(), lastSleepLog.getSleepStart(), sleepEndTime, lastSleepLog.getDuration(), lastSleepLog.getLogDate());
            Log.d("BootReceiver", "Updated sleep log end time to: " + sleepEndTime);
        }
    }
}