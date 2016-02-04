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

    //Costruttore Privato
    private DBAccess(Context context) {
        this.openHelper = new DBHelper(context);
    }

    //Ritorna una istanza di DBAccess
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

    //Tutte le Query seguenti operano sulla tabella "PianoDiStudi"

    //Query per ottenere la lista di tutti gli insegnamenti presenti nella tabella PianoDiStudi
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

    //Query per ottenere la lista di tutti gli insegnamenti presenti nel Piano di Studi dell'utente
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

    //Query per ottenere la lista di tutti gli insegnamenti NON presenti nel Piano di Studi dell'utente
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

    //Query per ottenere la lista di tutti gli insegnamenti che l'utente sta attualmente seguendo
    public List<String> getSeguiti(){
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT Insegnamento FROM PianoDiStudi WHERE NelPiano = 'SI' AND Voto = '' AND Seguito = 'SI'", null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            list.add(cursor.getString(0));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    //Query per ottenere la lista di tutti gli insegnamenti che l'utente NON sta attualmente seguendo
    public List<String> getNonSeguiti(){
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT Insegnamento FROM PianoDiStudi WHERE NelPiano = 'SI' AND Voto = '' AND Seguito = 'NO'", null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            list.add(cursor.getString(0));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    //Query per ottenere tutti gli insegnamenti che l'utente ha nel proprio Piano di Studi e non ha ancora superato
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

    //Query per ottenere tutti gli insegnamenti che l'utente ha nel proprio Piano di Studi, non ha ancora superato e non ha ancora prenotato
    public List<String> getPrenotabili(){
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT Insegnamento FROM PianoDiStudi WHERE NelPiano = 'SI' AND Voto = '' AND DataSuperamento = '' ", null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            list.add(cursor.getString(0));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    //Query per ottenere tutti gli insegnamenti che l'utente ha nel proprio Piano di Studio, non ha ancora superato ma ha già prenotato
    public List<String> getPrenotati(){
        List<String> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT Insegnamento FROM PianoDiStudi WHERE NelPiano = 'SI' AND Voto = '' AND DataSuperamento NOT = '' ", null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            list.add(cursor.getString(0));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    //Query per settare a 'SI' la colonna "NelPiano" di un determinato insegnamento
    public void setNelPiano(String insegnamento){
        database.execSQL("UPDATE PianoDiStudi SET NelPiano = 'SI' WHERE Insegnamento = '" + insegnamento + "'");
    }

    //Query per settare a 'NO' la colonna "NelPiano" di un determinato insegnamento
    public void setNelPianoNull(String insegnamento){
        database.execSQL("UPDATE PianoDiStudi SET NelPiano = 'NO' WHERE  Insegnamento = '" + insegnamento + "'");
    }

    //Query per settare a 'NO' la colonna "NelPiano" di tutti gli insegnamenti
    public void setNelPianoToZero(){
        database.execSQL("UPDATE PianoDiStudi SET NelPiano = 'NO' WHERE NelPiano = 'SI'");
    }

    //Query per settare il Voto di un determinato insegnamento
    public void setVotoEsameSuperato(String insegnamento, int voto){
        database.execSQL("UPDATE PianoDiStudi SET Voto = " + voto + " WHERE Insegnamento = '" + insegnamento + "'");
    }

    //Query per settare la DataSuperamento di un determinato insegnamento
    public void setDataEsameSuperato(String insegnamento, String data){     //testato
        database.execSQL("UPDATE PianoDiStudi SET DataSuperamento = '" + data + "' WHERE Insegnamento = '" + insegnamento + "'");
    }

    //Query per settare a 'SI' la colonna "Seguito" di un determinato insegnamento
    public void setSeguiti (String insegnamento ){
        database.execSQL("UPDATE PianoDiStudi SET Seguito = 'SI' WHERE Insegnamento = '" + insegnamento + "'" );
    }

    //Query per settare a 'NO' la colonna "Seguito" di un determinato insegnamento
    public void setSeguitiNull(String insegnamento){
        database.execSQL("UPDATE PianoDiStudi SET Seguito = 'NO' WHERE  Insegnamento = '" + insegnamento + "'");
    }

    //Query per settare a 'NO' la colonna "Seguito" di tutti gli insegnamenti
    public void setSeguitiToZero(){
        database.execSQL("UPDATE PianoDiStudi SET Seguito = 'NO' WHERE Seguito = 'SI'");
    }

    //Query per settare a ' ' il Voto di un determinato insegnamento
    public void setVotoNull(String insegnamento){
        database.execSQL("UPDATE PianoDiStudi SET Voto = '' WHERE  Insegnamento = '" + insegnamento + "'");
    }

    //Query per settare a ' ' la DataSuperamento di un determinato insegnamento
    public void setDataNull(String insegnamento){
        database.execSQL("UPDATE PianoDiStudi SET DataSuperamento = '' WHERE  Insegnamento = '" + insegnamento + "'");
    }

    //Query per settare a ' ' il Voto di tutti gli insegnamenti
    public void setVotoToZero(){
        database.execSQL("UPDATE PianoDiStudi SET Voto = ''");
    }

    //Query per settare a ' ' la DataSuperamento di tutti gli insegnamenti
    public void setDataToZero(){
        database.execSQL("UPDATE PianoDiStudi SET DataSuperamento = ''");
    }

    //Funzione per azzerare la carriera dell'utente. Raggruppa 4 Query in un unico metodo
    public void CancelCareer(){
        setNelPianoToZero();
        setSeguitiToZero();
        setVotoToZero();
        setDataToZero();
    }

    //Query per ottenere il Voto di un determiato insegnamento
    public Cursor getVotoEsameSuperato(String insegnamento){    //da testare
        Cursor voto;
        voto = database.rawQuery("SELECT Voto FROM PianoDiStudi WHERE Insegnamento = '" + insegnamento + "'", null);
        return voto;
    }

    //Query per ottenere la DataSuepramento di un determinato insegnamento
    public Cursor getDataEsameSuperato(String insegnamento){    //da testare
        Cursor data;
        data = database.rawQuery("SELECT DataSuperamento FROM PianoDiStudi WHERE Insegnamento = '" + insegnamento + "'", null);
        return data;
    }

    //Query per ottenere la somma dei CFU degli esami superati
    public  Cursor getCFUpassed(){
        Cursor cursor = database.rawQuery("SELECT SUM(CFU) FROM PianoDiStudi WHERE Voto < 32 and Voto > 17" , null);
        return cursor;
    }

    //Query per ottenere la somma dai CFU di tutti gli esami "NelPiano"
    public Cursor getCFUTotal(){
        Cursor cursor = database.rawQuery("SELECT SUM(CFU) FROM PianoDiStudi WHERE NelPiano = 'SI' " , null);
        return cursor;
    }

    //Query per ottenere il numero degli esami superati
    public Cursor countCFUPassed(){
        Cursor cursor = database.rawQuery("SELECT COUNT(Voto) FROM PianoDiStudi WHERE Voto < 32 and Voto > 17" , null);
        return cursor;
    }

    //Query per ottenere il numero degli esami ancora da superare
    public Cursor countExamsLost(){
        Cursor cursor = database.rawQuery("SELECT COUNT(Voto) FROM PianoDiStudi WHERE Voto = '' and NelPiano = 'SI'"  , null);
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

    //Query per ottenere "Insegnamento", "Voto" e "DataSuperamento" di tutti gli esami superati
    public Cursor cursorExamsPassed(){
        // il custom cursor richede una colonna id provo a rinominare quella di insegnamento
        Cursor cursor = database.rawQuery("SELECT Insegnamento as _id , Voto , DataSuperamento FROM PianoDiStudi WHERE Voto < 32 AND Voto > 17" , null);
        return  cursor;
    }

    //Query per ottenere la MediaPesata sugli esami superati
    public Cursor cursorMediaPesata(){
        Cursor cursor = database.rawQuery("SELECT  (sum(Voto * CFU) ) / sum(CFU) as MediaPesata FROM PianoDiStudi WHERE Voto < 32 AND Voto > 17 AND NelPiano = 'SI' " , null);
        return  cursor;
    }


    //Tutte le Query seguenti operano sulla tabella "AppelliEsami"

    //Query per ottenere Data, Ora e Aula del 1° Appello di un determinato insegnamento
    public Cursor getAppello1(String insegnamento){         //da testare
        Cursor cursor = database.rawQuery("SELECT Data_1, Ora_1, Aula_1 FROM AppelliEsami WHERE Insegnamento = '" + insegnamento + "'", null);
        return cursor;
    }

    //Query per ottenere Data, Ora e Aula del 2° Appello di un determinato insegnamento
    public Cursor getAppello2(String insegnamento){         //da testare
        Cursor cursor = database.rawQuery("SELECT Data_2, Ora_2, Aula_2 FROM AppelliEsami WHERE Insegnamento = '" + insegnamento + "'", null);
        return cursor;
    }
}
