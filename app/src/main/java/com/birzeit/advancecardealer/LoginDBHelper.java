package com.birzeit.advancecardealer;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.fragment.app.FragmentActivity;

public class LoginDBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "UserDB";
    private static final int DB_VERSION = 1;
    public static final String TABLE_NAME = "users";
    public static final String COL_EMAIL = "email";
    public static final String COL_ADMIN = "admin";
    public static final String COL_FIRSTNAME = "firstname";
    public static final String COL_LASTNAME = "lastname";
    public static final String COL_PASSWORD = "password";
    public static final String COL_GENDER = "gender";
    public static final String COL_PHONE = "phone";
    public static final String COL_COUNTRY = "country";
    public static final String COL_CITY = "city";
    public static final String COL_IMAGE_URL = "image_url";


    public LoginDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " (" +
                COL_EMAIL + " TEXT PRIMARY KEY, " +
                COL_FIRSTNAME + " TEXT, " +
                COL_LASTNAME + " TEXT, " +
                COL_PASSWORD + " TEXT, " +
                COL_GENDER + " TEXT, " +
                COL_PHONE + " TEXT, " +
                COL_COUNTRY + " TEXT, " +
                COL_CITY + " TEXT, " +
                COL_ADMIN + " INTEGER DEFAULT 0, " +
                COL_IMAGE_URL + " TEXT)";

        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean addUser(String email, String firstName, String lastName, String gender, String password, String country, String city, String phone, boolean isAdmin){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_EMAIL, email);
        values.put(COL_FIRSTNAME, firstName);
        values.put(COL_LASTNAME, lastName);
        values.put(COL_PASSWORD, password);
        values.put(COL_GENDER, gender);
        values.put(COL_PHONE, phone);
        values.put(COL_COUNTRY, country);
        values.put(COL_CITY, city);
        values.put(COL_ADMIN, isAdmin ? 1 : 0); // Convert boolean to integer
        values.put(COL_IMAGE_URL,"");
        long result = db.insert(TABLE_NAME, null, values);
        db.close();
        return result != -1;
    }

    public boolean checkUser(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " +
                COL_EMAIL + "=? AND " + COL_PASSWORD + "=?", new String[]{email, password});
        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        db.close();
        return exists;
    }

    public boolean checkAdmin(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " +
                COL_EMAIL + "=? AND " + COL_ADMIN + "=?", new String[]{email, "1"});
        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        db.close();
        return exists;
    }

    public boolean emailExist(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " +
                COL_EMAIL + "=?", new String[]{email});
        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        db.close();

        return exists;
    }

    public Cursor getUserDetails(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + COL_EMAIL + "=?", new String[]{email});
    }

    public boolean deleteUser(String email) {
        SQLiteDatabase db = this.getWritableDatabase();
        int deletedRows = db.delete(TABLE_NAME, COL_EMAIL + "=?", new String[]{email});
        db.close();
        return deletedRows > 0;
    }

    public boolean updateUserDetails(String email, String newFirstName, String newLastName, String newPhoneNumber, String newPassword) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_FIRSTNAME, newFirstName);
        contentValues.put(COL_LASTNAME, newLastName);
        contentValues.put(COL_PHONE,newPhoneNumber);
        contentValues.put(COL_PASSWORD, newPassword);
        int result = db.update(TABLE_NAME, contentValues, COL_EMAIL + "=?", new String[]{email});
        db.close();
        return result > 0;

    }

    public boolean updateUserDetailsWithImage(String email, String newFirstName, String newLastName, String newPhoneNumber, String newPassword, String imageUrl) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_FIRSTNAME, newFirstName);
        contentValues.put(COL_LASTNAME, newLastName);
        contentValues.put(COL_PHONE, newPhoneNumber);
        contentValues.put(COL_PASSWORD, newPassword);
        contentValues.put(COL_IMAGE_URL, imageUrl);
        int result = db.update(TABLE_NAME, contentValues, COL_EMAIL + "=?", new String[]{email});
        db.close();
        return result > 0;
    }
}
