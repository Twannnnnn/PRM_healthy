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
                "email TEXT UNIQUE NOT NULL, " +
                "password TEXT NOT NULL, " +
                "age INTEGER, " +
                "gender TEXT, " +
                "weight REAL, " +
                "height REAL, " +
                "bmi REAL, " +
                "body_fat_percentage REAL, " +
                "waist REAL, " +
                "neck REAL, " +
                "hips REAL" +
                ");");

        // Tạo bảng experts
        db.execSQL("CREATE TABLE IF NOT EXISTS experts (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT NOT NULL, " +
                "specialization TEXT NOT NULL" +
                ");");

        // Tạo bảng disease
        db.execSQL("CREATE TABLE IF NOT EXISTS disease (" +
                "disease_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT NOT NULL, " +
                "description TEXT" +
                ");");

        // Tạo bảng user_disease
        db.execSQL("CREATE TABLE IF NOT EXISTS user_disease (" +
                "user_id INTEGER, " +
                "disease_id INTEGER, " +
                "diagnosis_date DATE, " +
                "notes TEXT, " +
                "FOREIGN KEY (user_id) REFERENCES users(id), " +
                "FOREIGN KEY (disease_id) REFERENCES disease(disease_id), " +
                "PRIMARY KEY (user_id, disease_id)" +
                ");");
// Tạo bảng food
        db.execSQL("CREATE TABLE IF NOT EXISTS food (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT NOT NULL, " +
                "fat REAL, " +
                "protein REAL, " +
                "carbohydrates REAL, " +
                "fiber REAL, " + // Chất xơ
                "vitamins TEXT, " + // Vitamin
                "minerals TEXT " + // Khoáng chất
                ");");
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
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Xóa bảng cũ nếu tồn tại và tạo lại
        db.execSQL("DROP TABLE IF EXISTS user_disease");
        db.execSQL("DROP TABLE IF EXISTS disease");
        db.execSQL("DROP TABLE IF EXISTS experts");
        db.execSQL("DROP TABLE IF EXISTS users");
        db.execSQL("DROP TABLE IF EXISTS activity");
        db.execSQL("DROP TABLE IF EXISTS sleep_log");
        db.execSQL("DROP TABLE IF EXISTS reminders");
        db.execSQL("DROP TABLE IF EXISTS meal_log");
        db.execSQL("DROP TABLE IF EXISTS meal_plan");
        db.execSQL("DROP TABLE IF EXISTS nutrition_goals");
        db.execSQL("DROP TABLE IF EXISTS expert_advice");
        onCreate(db);
    }

    public void addUser(String name, String email, String password, Integer age, String gender, Float weight, Float height) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("email", email);
        values.put("password", password);
        values.put("age", age);
        values.put("gender", gender);
        values.put("weight", weight);
        values.put("height", height);
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
    public void addFood(String name, float fat, float protein, float carbohydrates, float fiber, String vitamins, String minerals) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("fat", fat);
        values.put("protein", protein);
        values.put("carbohydrates", carbohydrates);
        values.put("fiber", fiber);
        values.put("vitamins", vitamins);
        values.put("minerals", minerals);
        db.insert("food", null, values);
        db.close();
    }

    public Cursor getAllFood() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM food", null);
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
    public Cursor getAllLogs(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();

        // Truy vấn meal_log
        String mealLogQuery = "SELECT 'meal' AS log_type, meal_time AS log_time, meal_type AS title, food_items AS description " +
                "FROM meal_log WHERE user_id = ? " +
                "UNION ALL " +
                // Truy vấn sleep_log
                "SELECT 'sleep' AS log_type, sleep_start AS log_time, 'Giấc ngủ' AS title, sleep_end AS description " +
                "FROM sleep_log WHERE user_id = ? " +
                "UNION ALL " +
                // Truy vấn activity
                "SELECT 'activity' AS log_type, start_time AS log_time, name AS title, description " +
                "FROM activity WHERE user_id = ? " +
                "ORDER BY log_time";

        return db.rawQuery(mealLogQuery, new String[]{String.valueOf(userId), String.valueOf(userId), String.valueOf(userId)});
    }

    public boolean insertUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("name", user.getName());
        values.put("email", user.getEmail());
        values.put("password", user.getPassword());
        values.put("age", user.getAge());
        values.put("gender", user.getGender());
        values.put("weight", user.getWeight());
        values.put("height", user.getHeight());
        values.put("bmi", user.getBmi());
        values.put("body_fat_percentage", user.getBodyFatPercentage());
        values.put("waist", user.getWaist());
        values.put("neck", user.getNeck());
        values.put("hips", user.getHips());

        // Insert the new row and return the ID of the new row
        long userId = db.insert("users", null, values);
        db.close(); // Close the database connection
        return userId != -1;
    }

    public User getFirstUser() {
        SQLiteDatabase db = this.getReadableDatabase();
        User user = null;

        // Query to select the first user from the users table
        Cursor cursor = db.rawQuery("SELECT * FROM users LIMIT 1", null);

        // Check if the cursor has any results
        if (cursor != null && cursor.moveToFirst()) {
            user = new User();

            // Retrieve and check column indexes
            int idIndex = cursor.getColumnIndex("id");
            int nameIndex = cursor.getColumnIndex("name");
            int emailIndex = cursor.getColumnIndex("email");
            int passwordIndex = cursor.getColumnIndex("password");
            int ageIndex = cursor.getColumnIndex("age");
            int genderIndex = cursor.getColumnIndex("gender");
            int weightIndex = cursor.getColumnIndex("weight");
            int heightIndex = cursor.getColumnIndex("height");
            int bmiIndex = cursor.getColumnIndex("bmi");
            int bodyFatIndex = cursor.getColumnIndex("body_fat_percentage");
            int waistIndex = cursor.getColumnIndex("waist");
            int neckIndex = cursor.getColumnIndex("neck");
            int hipsIndex = cursor.getColumnIndex("hips");

            // Only set values if column indexes are valid (≥ 0)
            if (idIndex >= 0) user.setId(cursor.getInt(idIndex));
            if (nameIndex >= 0) user.setName(cursor.getString(nameIndex));
            if (emailIndex >= 0) user.setEmail(cursor.getString(emailIndex));
            if (passwordIndex >= 0) user.setPassword(cursor.getString(passwordIndex));
            if (ageIndex >= 0) user.setAge(cursor.getInt(ageIndex));
            if (genderIndex >= 0) user.setGender(cursor.getString(genderIndex));
            if (weightIndex >= 0) user.setWeight(cursor.getDouble(weightIndex));
            if (heightIndex >= 0) user.setHeight(cursor.getDouble(heightIndex));
            if (bmiIndex >= 0) user.setBmi(cursor.getDouble(bmiIndex));
            if (bodyFatIndex >= 0) user.setBodyFatPercentage(cursor.getDouble(bodyFatIndex));
            if (waistIndex >= 0) user.setWaist(cursor.getDouble(waistIndex));
            if (neckIndex >= 0) user.setNeck(cursor.getDouble(neckIndex));
            if (hipsIndex >= 0) user.setHips(cursor.getDouble(hipsIndex));
        }

        // Close cursor and database connection
        if (cursor != null) {
            cursor.close();
        }
        db.close();

        return user; // Return the User object or null if not found
    }

}