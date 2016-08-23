package com.example.simone.myuniversity;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Simone on 17/01/2016.
 */

// Database che gestisce i dati dell' utente
public class GestioneDBUtente {

    static final String KEY_RIGAID = "_id";
    static final String KEY_NOME = "nome";
    static final String KEY_COGNOME = "cognome";
    static final String KEY_MATRICOLA = "matricola";
    static final String KEY_USERNAME = "username";
    static final String KEY_PASSWORD = "password";
    static final String TAG = "GestioneDBUtente";
    static final String DATABASE_NOME = "MyUniversityUserDB";
    static final String DATABASE_TABELLA = "user";
    static final int DATABASE_VERSIONE = 1;

    static final String DATABASE_CREAZIONE = "create table user (_id integer primary key autoincrement, " + "nome text not null, cognome text not null, matricola text not null, username text not null, password text not null);";

    final Context context;
    DatabaseHelper DBHelper;
    SQLiteDatabase db;

    public GestioneDBUtente (Context ctx){
        this.context = ctx;
        DBHelper = new DatabaseHelper(context);
    }

    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context) {
            super(context,DATABASE_NOME,null,DATABASE_VERSIONE);
        }
        @Override
        public void onCreate(SQLiteDatabase db) {
            try {
                db.execSQL(DATABASE_CREAZIONE);
            }
            catch (SQLException e){
                e.printStackTrace();
            }
        }

        @Override
        public void onUpgrade (SQLiteDatabase db, int oldVersion, int newVersion){
            Log.v(TAG, "Upgrading database from" + oldVersion + " to " + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXIST user");
            onCreate(db);
        }
    }

    public GestioneDBUtente open() throws SQLException {
        db = DBHelper.getWritableDatabase();
        return this;
    }

    public void close(){
        DBHelper.close();
    }

    public Cursor get_User(long rigaID) throws SQLException {

        Cursor myCursor = db.query(true, DATABASE_TABELLA, new String[] {KEY_RIGAID, KEY_NOME, KEY_COGNOME, KEY_MATRICOLA, KEY_USERNAME, KEY_PASSWORD}, KEY_RIGAID + "=" + rigaID, null, null, null, null, null);
        if(myCursor != null){
            myCursor.moveToFirst();
        }
        return myCursor;
    }

    /*public Cursor get_AllUser(){
        return db.query(DATABASE_TABELLA, new String[] {KEY_RIGAID, KEY_NOME, KEY_COGNOME, KEY_MATRICOLA, KEY_USERNAME, KEY_PASSWORD}, null, null, null, null, null);
    }*/

    public long insertUser(String nome, String cognome, String matricola, String username, String password) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_NOME, nome);
        initialValues.put(KEY_COGNOME, cognome);
        initialValues.put(KEY_MATRICOLA, matricola);
        initialValues.put(KEY_USERNAME, username);
        initialValues.put(KEY_PASSWORD, password);
        return db.insert(DATABASE_TABELLA, null, initialValues);
    }

    public boolean cancellaUtente(long rigaID) {

        return db.delete(DATABASE_TABELLA, KEY_RIGAID + "=" + rigaID, null) > 0;
    }
}
