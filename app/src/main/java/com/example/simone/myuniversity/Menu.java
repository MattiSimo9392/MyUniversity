package com.example.simone.myuniversity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class Menu extends AppCompatActivity {

    TextView user, matricola;
    GestioneDBUtente databaseUtente, dbUtente;
    Cursor cursor , inizFinEsami;

    DBAccess db;
    long eventID;
    Uri uri;

    String nome, cognome, mat;

    SharedPreferences sharedPreferences;
    long id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        sharedPreferences = getSharedPreferences("_IdLog", MODE_PRIVATE);
        id = sharedPreferences.getLong("_IdLog", -1);

        user = (TextView) findViewById(R.id.tv_utente);
        matricola = (TextView) findViewById(R.id.tv_matricola);

        databaseUtente = new GestioneDBUtente(getApplicationContext());

        databaseUtente.open();
        cursor = databaseUtente.get_User(id);
        nome = cursor.getString(1);
        cognome = cursor.getString(2);
        mat = cursor.getString(3);
        databaseUtente.close();

        user.setText("Utente: " + nome + " " + cognome);
        matricola.setText("Matricola: " + mat);


    }

    public void onClick_modPS(View view) {
        startActivity(new Intent(getApplicationContext(), ModificaPianoStudi.class));
    }

    public void onClick_showCareer(View view) {
        startActivity(new Intent(getApplicationContext(), VisualizzaCarriera.class));
    }

    public void onClick_modSegui(View view) {
        startActivity(new Intent(getApplicationContext(), ModificaEsamiCheStaiSeguendo.class));
    }

    public void onClick_prenExam(View view) {
        startActivity(new Intent(getApplicationContext(), GestionePrenotazioneEsami.class));
    }

    public void onClick_regExam(View view) {
        startActivity(new Intent(getApplicationContext(), RegistrazioneEsami.class));
    }

    public void onClick_showOrari(View view) {
        startActivity(new Intent(getApplicationContext(), VisualizzaOrariLezioni.class));
    }

    public void onClick_showExam(View view) {
        startActivity(new Intent(getApplicationContext(), VisualizzaEsamiPrenotati.class));
    }

    public void onClick_CancelCareer(View view) {
        // Riportiamo la preference al valore iniziale in modo che compaia il bottone
        // collegato alla MainActivity e alla FifthRegistrationActivity

        SharedPreferences mprefs = getSharedPreferences("Registrazione", MODE_PRIVATE);
        SharedPreferences.Editor editor = mprefs.edit();
        editor.putString("Registrazione", "3");            // condizione per la ricomparsa del pulsante
        editor.apply();


        AlertDialog.Builder insert = new AlertDialog.Builder(Menu.this);
        insert.setTitle("ATTENZIONE!");
        insert.setMessage("Sei Sicuro di voler eliminare la tua carriera? \n Verranno cancellati anche le tue credenziali d'accesso e non potrai rieffettuare il Login \n L'operazione è Irreversibile!");
        insert.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DBAccess database = DBAccess.getInstance(getBaseContext());
                database.open();
                database.CancelCareer();
                database.close();
                dbUtente = new GestioneDBUtente(getApplicationContext());
                dbUtente.open();
                dbUtente.cancellaUtente(id);
                dbUtente.close();
                db = DBAccess.getInstance(getBaseContext());
                eraseCalendar(db);
                //startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
            }
        });
        insert.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
            }
        });
        Dialog insertDialog = insert.create();
        insertDialog.show();
    }


    public void onClick_SyncCalendar(View view) {
        db = DBAccess.getInstance(getBaseContext());
        eraseCalendar(db);
        syncCalendar();
    }
    //////////////////////////////////Funzione SyncCalendar/////////////////////////////////////////

    public void syncCalendar() {
        DBAccess db = DBAccess.getInstance(this);
        db.open();

        Cursor syncLun, syncMar, syncMer, syncGio, syncVen , syncExam;
        syncLun = db.getLun();
        syncLun.moveToFirst();
        syncMar = db.getMar();
        syncMar.moveToFirst();
        syncMer = db.getMer();
        syncMer.moveToFirst();
        syncGio = db.getGio();
        syncGio.moveToFirst();
        syncVen = db.getVen();
        syncVen.moveToFirst();
        syncExam = db.getBookedExams();
        syncExam.moveToFirst();

        // mi ricavo la data di fine per inerirla nella recurrence
        inizFinEsami = db.getInizFinEsami();
        inizFinEsami.moveToFirst();
        db.close();
        String finEsami = inizFinEsami.getString(1);
        Data fine = new Data();
        fine.DateSplitterV2(finEsami);
        String datarecurrence = fine.yearstr + fine.monthstr + fine.daystr;

        Toast.makeText(getBaseContext() , "Fine Lezioni : " + fine.yearstr + fine.monthstr  + fine.daystr,Toast.LENGTH_LONG).show();

        syncExams(syncExam);

        // Mi calcolo attraverso un dateSplitter la durata delle lezioni e la trasformo il millisecondi
        // moltiplicando per 60*60*1000 e aggiungo tale valore all'inizio che mi ricavo semplicemente estraendo la prima
        // parte di data (vedi DateSplitter)
        String recurrenceLun = "FREQ=WEEKLY;UNTIL="+datarecurrence+";BYDAY=MO;";
        syncDay(syncLun, recurrenceLun , db);
        String recurrenceMar = "FREQ=WEEKLY;UNTIL="+datarecurrence+";BYDAY=TU;";
        syncDay(syncMar, recurrenceMar , db);
        String recurrenceMer = "FREQ=WEEKLY;UNTIL="+datarecurrence+";BYDAY=WE;";
        syncDay(syncMer, recurrenceMer , db);
        String recurrenceGio = "FREQ=WEEKLY;UNTIL="+datarecurrence+";BYDAY=TH;";
        syncDay(syncGio, recurrenceGio , db);
        String recurrenceVen = "FREQ=WEEKLY;UNTIL="+datarecurrence+";BYDAY=FR;";
        syncDay(syncVen, recurrenceVen , db);


        Intent i = new Intent();
        ComponentName cn = new ComponentName("com.google.android.calendar", "com.android.calendar.LaunchActivity");
        i.setComponent(cn);
        startActivity(i);


    }
    ///////////////////////////////////////EraseCalendar////////////////////////////////////////////

    public void eraseCalendar(DBAccess db) {
        db.open();
        Cursor cursorEventID = db.getEventsID();
        cursorEventID.moveToNext();
        while (!cursorEventID.isAfterLast()) {
            Uri eventsUri = Uri.parse("content://com.android.calendar/events");
            Uri deleteEventUri = Uri.withAppendedPath(eventsUri, String.valueOf(cursorEventID.getLong(0)));
            getBaseContext().getContentResolver().delete(deleteEventUri, null, null);
            cursorEventID.moveToNext();
        }
        //Toast.makeText(getBaseContext(), "Ho cancellato tutto !!!!", Toast.LENGTH_LONG).show();

    }

    /////////////////////////////////////Classe Data////////////////////////////////////////////////
    public  class Data {
        int firsthour;
        int secondhour;
        int diff;
        int year , month , day;
        String yearstr;
        String monthstr;
        String daystr;
        int hourofexam;


        public void DateSplitter(String string) {
            String stringfirsthour = string.substring(0, string.indexOf("-"));
            String stringsecondhour = string.substring(string.indexOf("-") + 1, string.length());
            firsthour = Integer.parseInt(stringfirsthour);
            secondhour = Integer.parseInt(stringsecondhour);
            diff = secondhour - firsthour;

        }

        public void DateSplitterV2(String string){
            yearstr = string.substring(0 , 4);
            monthstr = string.substring(5 , 7);
            daystr = string.substring(8 , string.length());
            year = Integer.parseInt(yearstr);
            month = Integer.parseInt(monthstr);
            day = Integer.parseInt(daystr);
            hourofexam = 0;
        }
        public void DateSplitterV3(String string){
            String hourofexamstr = string.substring(0 , 2);
            hourofexam = Integer.parseInt(hourofexamstr);
        }

    }

    ///////////////////////////////////////SyncDay//////////////////////////////////////////////////
    public void syncDay(Cursor cursor, String recurrence , DBAccess db ) {
        Data dataLun = new Data();

        // controllo se il cursore è vuoto
        if (cursor == null) {
            Toast.makeText(getBaseContext(), "Cursore vuoto  ", Toast.LENGTH_LONG).show();
        } else {
            while (!cursor.isAfterLast()) {
                dataLun.DateSplitter(cursor.getString(3));

                String controllo = "Insegnamento : " + cursor.getString(2) + "\nAula : " + cursor.getString(1) + "\nOra : " + cursor.getString(3);

                //Toast.makeText(getBaseContext(), controllo, Toast.LENGTH_LONG).show();
                long startEvent = (dataLun.firsthour) * 60 * 60 * 1000;
                long endEvent = (dataLun.secondhour) * 60 * 60 * 1000;
                long dur = dataLun.diff * 60 * 60 * 1000;

                Calendar cal = Calendar.getInstance();
                db.open();
                // mi ricavo con un Cursor l'inizio e la fine degli esami e poi li imposto come inizio e come fine alla regola di recurrence

                String inizioEsami = inizFinEsami.getString(0);

                db.close();

                Data inizio = new Data();
                inizio.DateSplitterV2(inizioEsami);
                cal.set(inizio.year, inizio.month - 1, inizio.day - 1 , 0 , 0 , 0);

                startEvent = cal.getTimeInMillis() + startEvent;
                endEvent = cal.getTimeInMillis() + endEvent;
                ContentResolver cr = getContentResolver();
                ContentValues values = new ContentValues();

                values.put(CalendarContract.Events.CALENDAR_ID, 2);
                values.put(CalendarContract.Events.DTSTART, startEvent);
                values.put(CalendarContract.Events.DTEND, endEvent);
                //values.put(CalendarContract.Events.DURATION , dur);
                values.put(CalendarContract.Events.TITLE, "Lezione di : " + cursor.getString(2) + " in Aula : " + cursor.getString(1));
                //values.put(CalendarContract.Events.EVENT_TIMEZONE, "America/Los_Angeles");
                values.put(CalendarContract.Events.EVENT_TIMEZONE, "Italy/Rome"); // indenta corettamente i giorni della RRULE
                values.put(CalendarContract.Events.DESCRIPTION, cursor.getString(2) + "\nAula : " + cursor.getString(1));
                //Aggiungo una RRule per il controllo della cancellazione
                values.put(CalendarContract.Events.RRULE, recurrence);

                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                uri = cr.insert(CalendarContract.Events.CONTENT_URI, values);
                eventID = Long.parseLong(uri.getLastPathSegment());
                //Aggiungo il'eventID al database dei calendar events con descrizione
                db.open();
                String description = cursor.getString(2) + "-" + cursor.getString(1);
                db.WriteEventInDatabase(description, eventID);
                db.close();

                //Toast.makeText(getBaseContext(), "Lunedì Sicronizzato", Toast.LENGTH_LONG).show();

                cursor.moveToNext();

            }
        }
    }

    ///////////////////////////////////////////SyncExams//////////////////////////////////////
    public void syncExams(Cursor cursor) {
        Data exams = new Data();
        while (!cursor.isAfterLast()) {
            //exams.DateSplitter(cursor.getString(2));
            //long startEvent = (exams.firsthour) * 60 * 60 * 1000;
            //long endEvent = (exams.secondhour) * 60 * 60 * 1000;

            Calendar cal = Calendar.getInstance();
            exams.DateSplitterV2(cursor.getString(2));
            /////////////////////////////////////////////////////////////////////////////////////

            db.open();
            Cursor controlloDate = db.getExamsDate();
            controlloDate.moveToFirst();
            while (!controlloDate.isAfterLast()){
                if (cursor.getInt(0)==(controlloDate.getInt(0))){
                    if(cursor.getString(2).equals(controlloDate.getString(3))){
                        exams.DateSplitterV3(controlloDate.getString(controlloDate.getColumnIndex("Ora_1")));
                        controlloDate.moveToNext();
                    }else if(cursor.getString(2).equals(controlloDate.getString(5))){
                        exams.DateSplitterV3(controlloDate.getString(controlloDate.getColumnIndex("Ora_2")));
                        controlloDate.moveToNext();
                    }
                }else{
                    controlloDate.moveToNext();
                }
            }
            db.close();

            ///////////////////////////////////////////////////////////////////////////////////
            cal.set(exams.year, exams.month-1, exams.day , exams.hourofexam , 0 , 0);
            long startEvent = cal.getTimeInMillis();
            long endEvent = cal.getTimeInMillis() + 3*60*60*1000; // lo facciam durare 3 ore l'evento vista una media della durata degli esami
            ContentResolver cr = getContentResolver();
            ContentValues values = new ContentValues();

            values.put(CalendarContract.Events.CALENDAR_ID, 2);
            values.put(CalendarContract.Events.DTSTART, startEvent);
            values.put(CalendarContract.Events.DTEND, endEvent);
            //values.put(CalendarContract.Events.DURATION , dur);
            values.put(CalendarContract.Events.TITLE, "Esame WAAAAAAAAAAAAAAA!!!!");
            //values.put(CalendarContract.Events.EVENT_TIMEZONE, "America/Los_Angeles");
            values.put(CalendarContract.Events.EVENT_TIMEZONE, "Italy/Rome"); // indenta corettamente i giorni della RRULE
            values.put(CalendarContract.Events.DESCRIPTION, "Esame di : " + cursor.getString(1));

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            uri = cr.insert(CalendarContract.Events.CONTENT_URI, values);
            eventID = Long.parseLong(uri.getLastPathSegment());
            //Aggiungo il'eventID al database dei calendar events con descrizione
            db.open();
            String description = "Esame di : " + cursor.getString(1);
            db.WriteEventInDatabase(description, eventID);
            db.close();

            //Toast.makeText(getBaseContext(), "Esami Prenotati sincronizzati Sicronizzato", Toast.LENGTH_LONG).show();

            cursor.moveToNext();
        }
    }




    @Override
    public void onBackPressed()
    {
        AlertDialog.Builder exit = new AlertDialog.Builder(this);
        exit.setTitle("Sei sicuro di voler uscire da MyUniversity?");
        exit.setCancelable(false);
        exit.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
            }
        });
        exit.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("LOGOUT", true);
                startActivity(intent);
            }
        });
        Dialog exitDialog = exit.create();
        exitDialog.show();
    }
}
