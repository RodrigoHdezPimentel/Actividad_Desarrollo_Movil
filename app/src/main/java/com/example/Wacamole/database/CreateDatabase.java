package com.example.Wacamole.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class CreateDatabase extends SQLiteOpenHelper {
    private static final String DB_NAME = "Users";
    private static final int DB_VERSION = 1;


    public CreateDatabase(@Nullable Context context) {
        super(context, DB_NAME, null ,DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("Create table if not exists " + DB_NAME  +
                    "(ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "name VARCHAR(20) NOT NULL, " +
                    "surname VARCHAR(30))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
