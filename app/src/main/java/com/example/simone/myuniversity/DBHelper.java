package com.example.simone.myuniversity;

import android.content.Context;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

/**
 * Created by Tia on 19/01/2016.
 */
public class DBHelper extends SQLiteAssetHelper{
    private static final String DATABASE_NAME = "UniversityDatabaseTest.db";
    private static final int DATABASE_VERSION = 1;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
}
