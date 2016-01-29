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

    public List<String> getNonNelPiano(){
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT Insegnamento FROM PianoDiStudi WHERE NelPiano = 'NO'", null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            list.add(cursor.getString(0));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> getDaSeguire(){
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT Insegnamento FROM PianoDiStudi WHERE NelPiano = 'SI' AND Voto = '' ", null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            list.add(cursor.getString(0));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public void setNelPiano(String insegnamento){
        database.execSQL("UPDATE PianoDiStudi SET NelPiano = 'SI' WHERE Insegnamento = '" + insegnamento + "'");
    }

    public void setNelPianoNull(String insegnamento){
        database.execSQL("UPDATE PianoDiStudi SET NelPiano = 'NO' WHERE  Insegnamento = '" + insegnamento + "'");
    }

    public void setNelPianoToZero(){
        database.execSQL("UPDATE PianoDiStudi SET NelPiano = 'NO' WHERE NelPiano = 'SI'");
    }

    public void setVotoEsameSuperato(String insegnamento, int voto){        //testato
        database.execSQL("UPDATE PianoDiStudi SET Voto = " + voto + " WHERE Insegnamento = '" + insegnamento + "'");
    }

    public void setDataEsameSuperato(String insegnamento, String data){     //testato
        database.execSQL("UPDATE PianoDiStudi SET DataSuperamento = '" + data + "' WHERE Insegnamento = '" + insegnamento + "'");
    }

    public void setSeguiti (String insegnamento ){
        database.execSQL("UPDATE PianoDiStudi SET Seguito = 'SI' WHERE Insegnamento = '" + insegnamento + "'" );
    }

    public void setSeguitiNull(String insegnamento){
        database.execSQL("UPDATE PianoDiStudi SET Seguito = 'NO' WHERE  Insegnamento = '" + insegnamento + "'");
    }

    public void setSeguitiToZero(){
        database.execSQL("UPDATE PianoDiStudi SET Seguito = 'NO' WHERE Seguito = 'SI'");
    }

    public void setVotoNull(String insegnamento){
        database.execSQL("UPDATE PianoDiStudi SET Voto = '' WHERE  Insegnamento = '" + insegnamento + "'");
    }

    public void setDataNull(String insegnamento){
        database.execSQL("UPDATE PianoDiStudi SET DataSuperamento = '' WHERE  Insegnamento = '" + insegnamento + "'");
    }

    public void setVotoToZero(){
        database.execSQL("UPDATE PianoDiStudi SET Voto = ''");
    }

    public void setDataToZero(){
        database.execSQL("UPDATE PianoDiStudi SET DataSuperamento = ''");
    }

    // da testare
    public void CancelCareer(){
        setNelPianoToZero();
        setSeguitiToZero();
        setVotoToZero();
        setDataToZero();
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

    public  Cursor getCFUpassed(){
        Cursor cursor = database.rawQuery("SELECT SUM(CFU) FROM PianoDiStudi WHERE Voto < 32 and Voto > 17" , null);
        return cursor ;
    }

    public Cursor getCFUTotal(){
        Cursor cursor = database.rawQuery("SELECT SUM(CFU) FROM PianoDiStudi " , null);
        return cursor ;
    }

    public Cursor countCFUPassed(){
        Cursor cursor = database.rawQuery("SELECT COUNT(Voto) FROM PianoDiStudi WHERE Voto < 32 and Voto > 17" , null);
        return cursor ;
    }

    public Cursor countExamsLost(){
        Cursor cursor = database.rawQuery("SELECT COUNT(Voto) FROM PianoDiStudi WHERE Voto = ''" , null);
        return cursor ;
    }

    // non utilizzato eventualmente da cancellare
    public List<String> getExamsPassed() {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT Insegnamento FROM PianoDiStudi WHERE Voto < 32 AND Voto > 17 ", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(0));
            cursor.moveToNext();
        }
        cursor.close();
        return list;

    }

    public Cursor cursorExamsPassed(){
        Cursor cursor = database.rawQuery("SELECT Insegnamento , Voto FROM PianoDiStudi WHERE Voto < 32 AND Voto > 17" , null);
        return  cursor;
    }
}
