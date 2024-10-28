package com.example.prm_healthyapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBcontext extends SQLiteOpenHelper{
    private static final String DB_NAME = "Healthy";
    private static final int DB_VERSION = 1;

    public DBcontext(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS users (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name TEXT NOT NULL," +
                "email TEXT NOT NULL UNIQUE," +
                "password TEXT NOT NULL)");

        db.execSQL("CREATE TABLE IF NOT EXISTS experts (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name TEXT NOT NULL," +
                "specialization TEXT NOT NULL)");

        db.execSQL("CREATE TABLE IF NOT EXISTS activity (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "user_id INTEGER," +
                "name TEXT NOT NULL," +
                "description TEXT," +
                "start_time TEXT NOT NULL," +
                "end_time TEXT NOT NULL," +
                "reminder BOOLEAN DEFAULT 0," +
                "FOREIGN KEY (user_id) REFERENCES users(id))");

        db.execSQL("CREATE TABLE IF NOT EXISTS sleep_log (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "user_id INTEGER," +
                "sleep_start TEXT NOT NULL," +
                "sleep_end TEXT NOT NULL," +
                "duration REAL," +
                "log_date TEXT NOT NULL," +
                "FOREIGN KEY (user_id) REFERENCES users(id))");

        db.execSQL("CREATE TABLE IF NOT EXISTS reminders (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "user_id INTEGER," +
                "activity_id INTEGER," +
                "reminder_time TEXT NOT NULL," +
                "is_active BOOLEAN DEFAULT 1," +
                "FOREIGN KEY (user_id) REFERENCES users(id)," +
                "FOREIGN KEY (activity_id) REFERENCES activity(id))");

        db.execSQL("CREATE TABLE IF NOT EXISTS meal_log (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "user_id INTEGER," +
                "meal_type TEXT NOT NULL," +
                "food_items TEXT NOT NULL," +
                "total_calories REAL," +
                "meal_time TEXT NOT NULL," +
                "log_date TEXT NOT NULL," +
                "FOREIGN KEY (user_id) REFERENCES users(id))");

        db.execSQL("CREATE TABLE IF NOT EXISTS meal_plan (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "user_id INTEGER," +
                "meal_type TEXT NOT NULL," +
                "planned_food_items TEXT NOT NULL," +
                "planned_calories REAL," +
                "plan_date TEXT NOT NULL," +
                "FOREIGN KEY (user_id) REFERENCES users(id))");

        db.execSQL("CREATE TABLE IF NOT EXISTS nutrition_goals (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "user_id INTEGER," +
                "daily_calorie_goal REAL," +
                "daily_protein_goal REAL," +
                "daily_fat_goal REAL," +
                "set_date TEXT NOT NULL," +
                "FOREIGN KEY (user_id) REFERENCES users(id))");

        db.execSQL("CREATE TABLE IF NOT EXISTS expert_advice (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "user_id INTEGER," +
                "expert_id INTEGER," +
                "advice TEXT NOT NULL," +
                "advice_date TEXT NOT NULL," +
                "FOREIGN KEY (user_id) REFERENCES users(id)," +
                "FOREIGN KEY (expert_id) REFERENCES experts(id))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Handle database upgrade logic here
        db.execSQL("DROP TABLE IF EXISTS activity");
        db.execSQL("DROP TABLE IF EXISTS sleep_log");
        db.execSQL("DROP TABLE IF EXISTS reminders");
        db.execSQL("DROP TABLE IF EXISTS meal_log");
        db.execSQL("DROP TABLE IF EXISTS meal_plan");
        db.execSQL("DROP TABLE IF EXISTS nutrition_goals");
        db.execSQL("DROP TABLE IF EXISTS expert_advice");
        db.execSQL("DROP TABLE IF EXISTS users");
        db.execSQL("DROP TABLE IF EXISTS experts");
        onCreate(db);
    }
}
