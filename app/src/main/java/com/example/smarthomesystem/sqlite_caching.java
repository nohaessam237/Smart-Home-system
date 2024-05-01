package com.example.smarthomesystem;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class sqlite_caching extends SQLiteOpenHelper {
    public static final String COLUMN_ID = "ID";
    public static final String USERS_TABLE = "USERS";
    public static final String TEMP_TABLE = "TEMPERATURE";
    public static final String COLUMN_FULLNAME = "FULLNAME";
    public static final String COLUMN_USERNAME = "USERNAME";
    public static final String COLUMN_EMAIL_ADDRESS = "EMAIL_ADDRESS";
    public static final String COLUMN_MOBILE_NUM = "MOBILE_NUM";
    public static final String COLUMN_BIRTH_DATE = "BIRTH_DATE";
    public static final String COLUMN_PASSWORD = "PASSWORD";
    public static final String COLUMN_TEMP = "TEMP";

    public sqlite_caching(@Nullable Context context) {
        super(context, "users.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CreateTableStatement = "CREATE TABLE " + USERS_TABLE + " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_FULLNAME + " TXT, " + COLUMN_USERNAME + " TXT," +
                COLUMN_EMAIL_ADDRESS + " TXT, " + COLUMN_MOBILE_NUM + " TXT, " + COLUMN_BIRTH_DATE + " DATE, " + COLUMN_PASSWORD + " TXT)";
        db.execSQL(CreateTableStatement);
        String CreateTableStatement2 = "CREATE TABLE " + TEMP_TABLE + " (" + COLUMN_TEMP + "TEXT)";
        db.execSQL(CreateTableStatement2);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void addOne(String fullname, String username, String emailaddress, String mobilenum, String birthdate, String password){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_FULLNAME, fullname);
        cv.put(COLUMN_USERNAME, username);
        cv.put(COLUMN_EMAIL_ADDRESS, emailaddress);
        cv.put(COLUMN_MOBILE_NUM, mobilenum);
        cv.put(COLUMN_BIRTH_DATE, birthdate);
        cv.put(COLUMN_PASSWORD, password);

        db.insert(USERS_TABLE, null, cv);

    }
    public void cache_temp(int temp){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_TEMP, temp);

        db.insert(TEMP_TABLE, null, cv);
    }

}
