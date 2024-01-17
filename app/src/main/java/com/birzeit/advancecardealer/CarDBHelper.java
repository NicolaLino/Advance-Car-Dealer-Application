package com.birzeit.advancecardealer;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class CarDBHelper extends SQLiteOpenHelper{
    private static final String DB_NAME = "CarDB";
    private static final int DB_VERSION = 2;  // Increment the version when you make changes to the schema
    public static final String TABLE_RESERVATIONS = "reservations";
    public static final String TABLE_FAVORITES = "favorites";

    // Columns for the 'reservations' table
    public static final String COL_RESERVATION_ID = "reservationId";
    public static final String COL_USER_EMAIL = "user_email";
    public static final String COL_CAR_ID = "car_id";
    public static final String COL_RESERVATION_DATE = "reservation_date";

    // Columns for the 'favorites' table
    public static final String COL_FAVORITE_ID = "favoriteId";
    public static final String COL_USER_EMAIL_FAVORITES = "user_email";
    public static final String COL_CAR_ID_FAVORITES = "car_id";

    public CarDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createReservationsTable = "CREATE TABLE " + TABLE_RESERVATIONS + " (" +
                COL_RESERVATION_ID + " INTEGER PRIMARY KEY, " +
                COL_USER_EMAIL + " TEXT, " +
                COL_CAR_ID + " INTEGER, " +
                COL_RESERVATION_DATE + " TEXT)";
        db.execSQL(createReservationsTable);

        String createFavoritesTable = "CREATE TABLE " + TABLE_FAVORITES + " (" +
                COL_FAVORITE_ID + " INTEGER PRIMARY KEY, " +
                COL_USER_EMAIL_FAVORITES + " TEXT, " +
                COL_CAR_ID_FAVORITES + " INTEGER)";
        db.execSQL(createFavoritesTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RESERVATIONS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FAVORITES);
        onCreate(db);
    }

    public boolean addReservation(String userEmail, int carId, String reservationDate) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_USER_EMAIL, userEmail);
        values.put(COL_CAR_ID, carId);
        values.put(COL_RESERVATION_DATE, reservationDate);
        long result = db.insert(TABLE_RESERVATIONS, null, values);
        db.close();
        return result != -1;
    }

    public boolean isReservedByOtherUser(String userEmailToExclude, int carId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] projection = {COL_RESERVATION_ID}; // We only need to check if there's any reservation entry

        String selection = COL_CAR_ID + " = ? AND " + COL_USER_EMAIL + " != ?";
        String[] selectionArgs = {String.valueOf(carId), userEmailToExclude};

        Cursor cursor = db.query(
                TABLE_RESERVATIONS,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        int rowCount = cursor.getCount();
        cursor.close();
        db.close();

        return rowCount > 0; // Returns true if there is at least one reservation entry for the given car ID and a user email other than the given email
    }

    public boolean isReserved(String userEmail, int carId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] projection = {COL_RESERVATION_ID}; // We only need to check if there's any reservation entry

        String selection = COL_USER_EMAIL + " = ? AND " + COL_CAR_ID + " = ?";
        String[] selectionArgs = {userEmail, String.valueOf(carId)};

        Cursor cursor = db.query(
                TABLE_RESERVATIONS,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        int rowCount = cursor.getCount();
        cursor.close();
        db.close();

        return rowCount > 0; // Returns true if there is at least one reservation entry for the given user email and car ID
    }


    public boolean deleteReservation(String userEmail, int carId) {
        SQLiteDatabase db = this.getWritableDatabase();
        String whereClause = COL_USER_EMAIL + " = ? AND " + COL_CAR_ID + " = ?";
        String[] whereArgs = {userEmail, String.valueOf(carId)};
        int result = db.delete(TABLE_RESERVATIONS, whereClause, whereArgs);
        db.close();
        return result > 0; // Returns true if at least one row was affected (deleted)
    }



    public boolean addFavorite(String userEmail, int carId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_USER_EMAIL_FAVORITES, userEmail);
        values.put(COL_CAR_ID_FAVORITES, carId);
        long result = db.insert(TABLE_FAVORITES, null, values);
        db.close();
        return result != -1;
    }

    // checking favorites
    public boolean isFavorite(String userEmail, int carId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {COL_FAVORITE_ID};
        String selection = COL_USER_EMAIL_FAVORITES + " = ? AND " + COL_CAR_ID_FAVORITES + " = ?";
        String[] selectionArgs = {userEmail, String.valueOf(carId)};
        Cursor cursor = db.query(TABLE_FAVORITES, columns, selection, selectionArgs, null, null, null);
        boolean isFavorite = cursor.moveToFirst();
        cursor.close();
        db.close();
        return isFavorite;
    }

    public boolean removeFavorite(String userEmail, int carId) {
        SQLiteDatabase db = this.getWritableDatabase();
        String whereClause = COL_USER_EMAIL_FAVORITES + " = ? AND " + COL_CAR_ID_FAVORITES + " = ?";
        String[] whereArgs = {userEmail, String.valueOf(carId)};
        int result = db.delete(TABLE_FAVORITES, whereClause, whereArgs);
        db.close();
        return result > 0;
    }

}
