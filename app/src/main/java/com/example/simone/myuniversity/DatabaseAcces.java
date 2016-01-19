package com.example.simone.myuniversity;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseAcces {
    private SQLiteOpenHelper openHelper;
    private SQLiteDatabase database;
    private static DatabaseAcces instance;

    private DatabaseAcces(Context context){
        this.openHelper = new DatabaseOpenHelper(context);
    }

    public static DatabaseAcces getInstance(Context context){
        if (instance == null){
            instance = new DatabaseAcces(context);
        }
        return instance;
    }

    public void open(){
        this.database = openHelper.getWritableDatabase();
    }

    public void close() {
        if (database != null) {
            this.database.close();
        }
    }

    public List<String> getQuotes() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("Select Insegnamento FROM PianoDiStudi", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            list.add(cursor.getString(0));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }
}
