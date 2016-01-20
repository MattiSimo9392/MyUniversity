package com.example.simone.myuniversity;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tia on 19/01/2016.
 */
public class DBAccess {
    private SQLiteOpenHelper openHelper;
    private SQLiteDatabase database;
    private static DBAccess instance;



     // Costruttore Privato
     // @param context

    private DBAccess(Context context) {
        this.openHelper = new DatabaseOpenHelper(context);
    }

    //Ritorna una istanza di DBAccess

     // @param context the Context
     // @return the instance of DabaseAccess

    public static DBAccess getInstance(Context context) {
        if (instance == null) {
            instance = new DBAccess(context);
        }
        return instance;
    }

    //Apertura Database
    public void open() {
        this.database = openHelper.getWritableDatabase();
    }

    //Chiusura Database
    public void close() {
        if (database != null) {
            this.database.close();
        }
    }

    /**
     * Read all quotes from the database.
     *
     * @return a List of quotes
     */
    public List<String> getQuotes() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT Insegnamento FROM PianodiStudi", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(0));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getSeguiti(){
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT Insegnamento FROM PianoDiStudi WHERE Seguito = 'SI'" , null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            list.add(cursor.getString(0));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public void setSeguiti(String insegnamento){
        //String query = "UPDATE PianoDiStudi SET Seguito = SI WHERE Insegnamento = ? ";
        //Cursor cursor = database.rawQuery(query ,new String[] { insegnamento }  );
        //cursor.moveToFirst();
        //cursor.close();
        database.execSQL("UPDATE PianoDiStudi SET Seguito = 'SI' WHERE Insegnamento = '" + insegnamento + "'");
    }

    public void setSeguitiNull(String insegnamento){
       // String query = "UPDATE PianoDiStudi SET Seguito = 'NO' WHERE = Insegnamento = '" + insegnamento + "'";
        //Cursor cursor = database.rawQuery(query ,null  );
       // cursor.moveToFirst();
       // cursor.close();
        database.execSQL("UPDATE PianoDiStudi SET Seguito = 'NO' WHERE  Insegnamento = '" + insegnamento + "'");

    }


}
