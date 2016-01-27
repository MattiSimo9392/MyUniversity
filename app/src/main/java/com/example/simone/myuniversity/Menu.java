package com.example.simone.myuniversity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Menu extends AppCompatActivity {

    TextView user, matricola;
    GestioneDBUtente databaseUtente, dbUtente;
    Cursor cursor;

    String nome, cognome, mat;

    SharedPreferences sharedPreferences;
    long id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        sharedPreferences = getSharedPreferences("_IdLog", MODE_PRIVATE);
        id = sharedPreferences.getLong("_IdLog", -1);

        user = (TextView)findViewById(R.id.tv_utente);
        matricola = (TextView)findViewById(R.id.tv_matricola);

        databaseUtente = new GestioneDBUtente(getApplicationContext());

        databaseUtente.open();
        cursor = databaseUtente.get_User(id);
        nome = cursor.getString(1);
        cognome = cursor.getString(2);
        mat = cursor.getString(3);
        databaseUtente.close();

        user.setText("Utente: " + nome + " " + cognome);
        matricola.setText("Matricola: "+ mat);

    }

    public void onClick_CancelCareer(View view){
        // Riportiamo la preference al valore iniziale in modo che compaia il bottone
        /*
        SharedPreferences mprefs = getSharedPreferences("Registrazione" , MODE_PRIVATE);
        SharedPreferences.Editor editor = mprefs.edit();
        editor.putString("Registrazione" , "3");            // condizione per la ricomparsa del pulsante
        editor.apply();
        */

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
