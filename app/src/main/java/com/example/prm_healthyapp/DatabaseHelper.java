package com.example.prm_healthyapp;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "health_management.db";
    private static final int DATABASE_VERSION = 16;

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
                "food_mass TEXT NOT NULL, " +
                "total_calories REAL, " +
                "total_protein REAL, " + // Add this line
                "total_fat REAL, " + // Add this line
                "total_carbohydrates REAL, " + // Add this line
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
        db.execSQL("CREATE TABLE IF NOT EXISTS nutrition_log (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "meal_log_id INTEGER, " +
                "food_name TEXT NOT NULL, " +
                "calories REAL, " +
                "fat REAL, " +
                "protein REAL, " +
                "carbohydrates REAL, " +
                "fiber REAL, " +
                "vitamins TEXT, " +
                "minerals TEXT, " +
                "FOREIGN KEY (meal_log_id) REFERENCES meal_log(id)" +
                ");");
        addDemoData(db);
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
    public void addMealLog(int userId, String mealType, String foodItems, float totalCalories, float totalProtein, float totalFat, float totalCarbohydrates, String mealTime, String logDate) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("user_id", userId);
        values.put("meal_type", mealType);
        values.put("food_items", foodItems);
        values.put("total_calories", totalCalories);
        values.put("total_protein", totalProtein); // Thêm protein
        values.put("total_fat", totalFat); // Thêm chất béo
        values.put("total_carbohydrates", totalCarbohydrates); // Thêm carbohydrate
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

    public Cursor getAllActivities() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM activity", null);
    }

    public ActivityModel getActivityById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM activity WHERE id = ?", new String[]{String.valueOf(id)});
        if (cursor != null && cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex("id");
            int userIdIndex = cursor.getColumnIndex("user_id");
            int nameIndex = cursor.getColumnIndex("name");
            int descriptionIndex = cursor.getColumnIndex("description");
            int startTimeIndex = cursor.getColumnIndex("start_time");
            int endTimeIndex = cursor.getColumnIndex("end_time");
            int reminderIndex = cursor.getColumnIndex("reminder");

            ActivityModel activity = new ActivityModel(
                    idIndex >= 0 ? cursor.getInt(idIndex) : 0,
                    userIdIndex >= 0 ? cursor.getInt(userIdIndex) : 0,
                    nameIndex >= 0 ? cursor.getString(nameIndex) : "",
                    descriptionIndex >= 0 ? cursor.getString(descriptionIndex) : "",
                    startTimeIndex >= 0 ? cursor.getString(startTimeIndex) : "",
                    endTimeIndex >= 0 ? cursor.getString(endTimeIndex) : "",
                    reminderIndex >= 0 && cursor.getInt(reminderIndex) == 1
            );
            cursor.close();
            return activity;
        }
        return null;
    }

    public void updateActivity(int id, String name, String description, String startTime, String endTime) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("description", description);
        values.put("start_time", startTime);
        values.put("end_time", endTime);
        db.update("activity", values, "id = ?", new String[]{String.valueOf(id)});
        db.close();
    }

    public void deleteActivity(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("activity", "id = ?", new String[]{String.valueOf(id)});
        db.close();
    }

    public Cursor getAllSleepLogs() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM sleep_log", null);
    }

    public SleepLogModel getSleepLogById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM sleep_log WHERE id = ?", new String[]{String.valueOf(id)});

        if (cursor != null && cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex("id");
            int userIdIndex = cursor.getColumnIndex("user_id");
            int sleepStartIndex = cursor.getColumnIndex("sleep_start");
            int sleepEndIndex = cursor.getColumnIndex("sleep_end");
            int durationIndex = cursor.getColumnIndex("duration");
            int logDateIndex = cursor.getColumnIndex("log_date");

            SleepLogModel sleepLog = new SleepLogModel(
                    idIndex != -1 ? cursor.getInt(idIndex) : -1,
                    userIdIndex != -1 ? cursor.getInt(userIdIndex) : -1,
                    sleepStartIndex != -1 ? cursor.getString(sleepStartIndex) : null,
                    sleepEndIndex != -1 ? cursor.getString(sleepEndIndex) : null,
                    durationIndex != -1 ? cursor.getFloat(durationIndex) : 0.0f,
                    logDateIndex != -1 ? cursor.getString(logDateIndex) : null
            );
            cursor.close();
            return sleepLog;
        }

        // Close cursor if it's not null to avoid memory leaks
        if (cursor != null) {
            cursor.close();
        }

        return null;
    }

    public void updateSleepLog(int id, String sleepStart, String sleepEnd, float duration, String logDate) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("sleep_start", sleepStart);
        values.put("sleep_end", sleepEnd);
        values.put("duration", duration);
        values.put("log_date", logDate);
        db.update("sleep_log", values, "id = ?", new String[]{String.valueOf(id)});
        db.close();
    }

    public void deleteSleepLog(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("sleep_log", "id = ?", new String[]{String.valueOf(id)});
        db.close();
    }

    public boolean checkSleepLogExists(String logDate) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM sleep_log WHERE log_date = ?", new String[]{logDate});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    public SleepLogModel getLastSleepLog() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM sleep_log ORDER BY log_date DESC LIMIT 1", null);
        if (cursor != null && cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex("id");
            int userIdIndex = cursor.getColumnIndex("user_id");
            int sleepStartIndex = cursor.getColumnIndex("sleep_start");
            int sleepEndIndex = cursor.getColumnIndex("sleep_end");
            int durationIndex = cursor.getColumnIndex("duration");
            int logDateIndex = cursor.getColumnIndex("log_date");

            SleepLogModel sleepLog = new SleepLogModel(
                    idIndex != -1 ? cursor.getInt(idIndex) : -1,
                    userIdIndex != -1 ? cursor.getInt(userIdIndex) : -1,
                    sleepStartIndex != -1 ? cursor.getString(sleepStartIndex) : null,
                    sleepEndIndex != -1 ? cursor.getString(sleepEndIndex) : null,
                    durationIndex != -1 ? cursor.getFloat(durationIndex) : 0.0f,
                    logDateIndex != -1 ? cursor.getString(logDateIndex) : null
            );
            cursor.close();
            return sleepLog;
        }
        return null;
    }

    public void addReminder(int userId, int activityId, String reminderTime) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("user_id", userId);
        values.put("activity_id", activityId);
        values.put("reminder_time", reminderTime);
        values.put("is_active", true);
        db.insert("reminders", null, values);
        db.close();
    }

    public Cursor getAllReminders(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM reminders WHERE user_id = ?", new String[]{String.valueOf(userId)});
    }

    public Reminder getReminderForActivity(int activityId, int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM reminders WHERE activity_id = ? AND user_id = ?",
                new String[]{String.valueOf(activityId), String.valueOf(userId)});

        if (cursor != null && cursor.moveToFirst()) {
            @SuppressLint("Range") Reminder reminder = new Reminder(
                    cursor.getInt(cursor.getColumnIndex("id")),
                    cursor.getInt(cursor.getColumnIndex("user_id")),
                    cursor.getInt(cursor.getColumnIndex("activity_id")),
                    cursor.getString(cursor.getColumnIndex("reminder_time")),
                    cursor.getInt(cursor.getColumnIndex("is_active")) == 1
            );
            cursor.close();
            return reminder;
        }
        return null;
    }
    private void addDemoData(SQLiteDatabase db) {

            ContentValues userValues = new ContentValues();
            userValues.put("name", "User ");
            userValues.put("email", "user@example.com");
            userValues.put("password", "password" );
            userValues.put("age", 20 );
            userValues.put("gender",  "Male" );
            userValues.put("weight", 60 );
            userValues.put("height", 150 );
            db.insert("users", null, userValues);


        // Manually add meal logs
        addMealLog(db, 1, "Breakfast", "Eggs, Bread", "150g", 300, "08:00", "2024-10-29");
        addMealLog(db, 1, "Lunch", "Chicken, Rice", "200g", 500, "12:00", "2024-10-28");
        addMealLog(db, 1, "Dinner", "Fish, Salad", "250g", 400, "18:00", "2024-10-28");
        addMealLog(db, 1, "Breakfast", "Pasta, Cheese", "180g", 350, "07:30", "2024-10-27");
        addMealLog(db, 1, "Lunch", "Apples, Yogurt", "160g", 250, "12:30", "2024-10-27");
        addMealLog(db, 1, "Dinner", "Nuts, Bananas", "100g", 200, "19:00", "2024-10-27");
        addMealLog(db, 1, "Snack", "Tomatoes, Garlic", "120g", 150, "15:00", "2024-10-26");
        addMealLog(db, 1, "Lunch", "Carrots, Broccoli", "140g", 220, "12:15", "2024-10-26");
        addMealLog(db, 1, "Dinner", "Peppers, Onions", "170g", 300, "18:45", "2024-10-26");
        addMealLog(db, 1, "Breakfast", "Spinach, Yogurt", "130g", 200, "09:00", "2024-10-25");

        // Add demo sleep logs
        addSleepLog(db, 1, "22:00", "06:00", 8.0f, "2024-10-30");
        addSleepLog(db, 1, "23:00", "07:00", 8.0f, "2024-10-30");
        addSleepLog(db, 1, "21:30", "05:30", 8.0f, "2024-10-30");
        // Continue adding sleep logs as needed...
    }

    private void addMealLog(SQLiteDatabase db, int userId, String mealType, String foodItems, String foodMass, float totalCalories, String mealTime, String logDate) {
        ContentValues mealLogValues = new ContentValues();
        mealLogValues.put("user_id", userId);
        mealLogValues.put("meal_type", mealType);
        mealLogValues.put("food_items", foodItems);
        mealLogValues.put("food_mass", foodMass);
        mealLogValues.put("total_calories", totalCalories);
        mealLogValues.put("meal_time", mealTime);
        mealLogValues.put("log_date", logDate);
        db.insert("meal_log", null, mealLogValues);
    }

    private void addSleepLog(SQLiteDatabase db, int userId, String sleepStart, String sleepEnd, float duration, String logDate) {
        ContentValues sleepLogValues = new ContentValues();
        sleepLogValues.put("user_id", userId);
        sleepLogValues.put("sleep_start", sleepStart);
        sleepLogValues.put("sleep_end", sleepEnd);
        sleepLogValues.put("duration", duration);
        sleepLogValues.put("log_date", logDate);
        db.insert("sleep_log", null, sleepLogValues);
    }
    public void updateReminder(int id, String reminderTime) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("reminder_time", reminderTime);
        db.update("reminders", values, "id = ?", new String[]{String.valueOf(id)});
        db.close();
    }

    public void deleteReminder(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("reminders", "id = ?", new String[]{String.valueOf(id)});
        db.close();
    }

}