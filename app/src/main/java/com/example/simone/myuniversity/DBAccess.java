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
        this.openHelper = new DBHelper(context);
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

    public List<String> getNelPiano(){
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT Insegnamento FROM PianoDiStudi WHERE NelPiano = 'SI'" , null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            list.add(cursor.getString(0));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public void setNelPiano(String insegnamento){
        //String query = "UPDATE PianoDiStudi SET Seguito = SI WHERE Insegnamento = ? ";
        //Cursor cursor = database.rawQuery(query ,new String[] { insegnamento }  );
        //cursor.moveToFirst();
        //cursor.close();
        database.execSQL("UPDATE PianoDiStudi SET NelPiano = 'SI' WHERE Insegnamento = '" + insegnamento + "'");
    }

    public void setNelPianoNull(String insegnamento){
       // String query = "UPDATE PianoDiStudi SET Seguito = 'NO' WHERE = Insegnamento = '" + insegnamento + "'";
        //Cursor cursor = database.rawQuery(query ,null  );
       // cursor.moveToFirst();
       // cursor.close();
        database.execSQL("UPDATE PianoDiStudi SET NelPiano = 'NO' WHERE  Insegnamento = '" + insegnamento + "'");

    }

    public void setNelPianoToZero(){
        database.execSQL("UPDATE PianoDiStudi SET NelPiano = 'NO' WHERE NelPiano = 'SI'");
    }

    public void setVotoEsameSuperato(String insegnamento, int voto){        //testato
        database.execSQL("UPDATE PianoDiStudi SET Voto = '" + voto + "' WHERE Insegnamento = '" + insegnamento + "'");
    }

    public void setDataEsameSuperato(String insegnamento, String data){     //testato
        database.execSQL("UPDATE PianoDiStudi SET DataSuperamento = '" + data + "' WHERE Insegnamento = '" + insegnamento + "'");
    }

    public Cursor getVotoEsameSuperato(String insegnamento){    //da testare
        Cursor voto;
        voto = database.rawQuery("SELECT Voto FROM PianoDiStudi WHERE Insegnamento = '" + insegnamento + "'", null);
        return voto;
    }

    public Cursor getDataEsameSuperato(String insegnamento){    //da testare
        Cursor data;
        data = database.rawQuery("SELECT DataSuperamento FROM PianoDiStudi WHERE Insegnamento = '" + insegnamento + "'", null);
        return data;
    }




}
