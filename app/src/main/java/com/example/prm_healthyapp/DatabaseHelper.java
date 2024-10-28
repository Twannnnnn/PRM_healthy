package com.example.prm_healthyapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "health_management.db";
    private static final int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Tạo bảng users
        db.execSQL("CREATE TABLE IF NOT EXISTS users (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT NOT NULL, " +
                "email TEXT NOT NULL UNIQUE, " +
                "password TEXT NOT NULL)");

        // Tạo các bảng khác
        db.execSQL("CREATE TABLE IF NOT EXISTS activity (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "user_id INTEGER, " +
                "name TEXT NOT NULL, " +
                "description TEXT, " +
                "start_time TEXT NOT NULL, " +
                "end_time TEXT NOT NULL, " +
                "reminder BOOLEAN DEFAULT 0, " +
                "FOREIGN KEY (user_id) REFERENCES users(id))");

        db.execSQL("CREATE TABLE IF NOT EXISTS sleep_log (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "user_id INTEGER, " +
                "sleep_start TEXT NOT NULL, " +
                "sleep_end TEXT NOT NULL, " +
                "duration REAL, " +
                "log_date TEXT NOT NULL, " +
                "FOREIGN KEY (user_id) REFERENCES users(id))");

        db.execSQL("CREATE TABLE IF NOT EXISTS reminders (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "user_id INTEGER, " +
                "activity_id INTEGER, " +
                "reminder_time TEXT NOT NULL, " +
                "is_active BOOLEAN DEFAULT 1, " +
                "FOREIGN KEY (user_id) REFERENCES users(id), " +
                "FOREIGN KEY (activity_id) REFERENCES activity(id))");

        db.execSQL("CREATE TABLE IF NOT EXISTS meal_log (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "user_id INTEGER, " +
                "meal_type TEXT NOT NULL, " +
                "food_items TEXT NOT NULL, " +
                "total_calories REAL, " +
                "meal_time TEXT NOT NULL, " +
                "log_date TEXT NOT NULL, " +
                "FOREIGN KEY (user_id) REFERENCES users(id))");

        db.execSQL("CREATE TABLE IF NOT EXISTS meal_plan (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "user_id INTEGER, " +
                "meal_type TEXT NOT NULL, " +
                "planned_food_items TEXT NOT NULL, " +
                "planned_calories REAL, " +
                "plan_date TEXT NOT NULL, " +
                "FOREIGN KEY (user_id) REFERENCES users(id))");

        db.execSQL("CREATE TABLE IF NOT EXISTS nutrition_goals (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "user_id INTEGER, " +
                "daily_calorie_goal REAL, " +
                "daily_protein_goal REAL, " +
                "daily_fat_goal REAL, " +
                "set_date TEXT NOT NULL, " +
                "FOREIGN KEY (user_id) REFERENCES users(id))");

        db.execSQL("CREATE TABLE IF NOT EXISTS expert_advice (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "user_id INTEGER, " +
                "expert_id INTEGER, " +
                "advice TEXT NOT NULL, " +
                "advice_date TEXT NOT NULL, " +
                "FOREIGN KEY (user_id) REFERENCES users(id), " +
                "FOREIGN KEY (expert_id) REFERENCES experts(id))");

        db.execSQL("CREATE TABLE IF NOT EXISTS experts (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT NOT NULL, " +
                "specialization TEXT NOT NULL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Xóa bảng cũ nếu tồn tại và tạo lại
        db.execSQL("DROP TABLE IF EXISTS users");
        db.execSQL("DROP TABLE IF EXISTS activity");
        db.execSQL("DROP TABLE IF EXISTS sleep_log");
        db.execSQL("DROP TABLE IF EXISTS reminders");
        db.execSQL("DROP TABLE IF EXISTS meal_log");
        db.execSQL("DROP TABLE IF EXISTS meal_plan");
        db.execSQL("DROP TABLE IF EXISTS nutrition_goals");
        db.execSQL("DROP TABLE IF EXISTS expert_advice");
        db.execSQL("DROP TABLE IF EXISTS experts");
        onCreate(db);
    }
    public void addUser(String name, String email, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("email", email);
        values.put("password", password);
        db.insert("users", null, values);
        db.close();
    }

    public void addActivity(int userId, String name, String description, String startTime, String endTime) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("user_id", userId);
        values.put("name", name);
        values.put("description", description);
        values.put("start_time", startTime);
        values.put("end_time", endTime);
        db.insert("activity", null, values);
        db.close();
    }
    public Cursor getAllUsers() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM users", null);
    }

    public Cursor getActivityByUser(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM activity WHERE user_id = ?", new String[]{String.valueOf(userId)});
    }
    public void updateUser(int id, String name, String email, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("email", email);
        values.put("password", password);
        db.update("users", values, "id = ?", new String[]{String.valueOf(id)});
        db.close();
    }
    public void deleteUser(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("users", "id = ?", new String[]{String.valueOf(id)});
        db.close();
    }
    public void addMealLog(int userId, String mealType, String foodItems, float totalCalories, String mealTime, String logDate) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("user_id", userId);
        values.put("meal_type", mealType);
        values.put("food_items", foodItems);
        values.put("total_calories", totalCalories);
        values.put("meal_time", mealTime);
        values.put("log_date", logDate);
        db.insert("meal_log", null, values);
        db.close();
    }
    public Cursor getAllMealLogs(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM meal_log WHERE user_id = ?", new String[]{String.valueOf(userId)});
    }
    public float getTotalCalories(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT SUM(total_calories) FROM meal_log WHERE user_id = ?", new String[]{String.valueOf(userId)});
        if (cursor.moveToFirst()) {
            return cursor.getFloat(0);
        }
        cursor.close();
        return 0;
    }
    public void addSleepLog(int userId, String sleepStart, String sleepEnd, float duration, String logDate) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("user_id", userId);
        values.put("sleep_start", sleepStart);
        values.put("sleep_end", sleepEnd);
        values.put("duration", duration);
        values.put("log_date", logDate);
        db.insert("sleep_log", null, values);
        db.close();
    }
    public Cursor getAllSleepLogs(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM sleep_log WHERE user_id = ?", new String[]{String.valueOf(userId)});
    }
    public float getTotalSleepDuration(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT SUM(duration) FROM sleep_log WHERE user_id = ?", new String[]{String.valueOf(userId)});
        if (cursor.moveToFirst()) {
            return cursor.getFloat(0);
        }
        cursor.close();
        return 0;
    }
}