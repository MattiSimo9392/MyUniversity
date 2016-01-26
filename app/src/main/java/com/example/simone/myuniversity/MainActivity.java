package com.example.simone.myuniversity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class MainActivity extends Activity {

    EditText et_username, et_password;
    GestioneDBUtente dbUtente;
    Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button registration = (Button)findViewById(R.id.btn_registrati);

        et_username = (EditText) findViewById(R.id.et_usernameLog);
        et_password = (EditText) findViewById(R.id.et_passwordLog);
        dbUtente = new GestioneDBUtente(getApplicationContext());

        //Questa parte di codice viene chiamata quando si preme il pulsante indietro e si conferma
        //di voler chiudere l'app
        if (getIntent().getBooleanExtra("LOGOUT", false))
        {
            finish();
        }

        // impostazione preferences con la FifthRegistrationActivity
        /*
        SharedPreferences mprefs = getSharedPreferences("Registrazione" , MODE_PRIVATE);
        String string = mprefs.getString("Registrazione" , "");
        if(string.equals("2")){registration.setVisibility(View.GONE);}
        else{registration.setVisibility(View.VISIBLE);}
        */
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
        //
    }

    public void onClick_login(View view){

        String un = et_username.getText().toString();
        String pw = et_password.getText().toString();

        dbUtente.open();

        cursor = dbUtente.get_AllUser();
        if (cursor.moveToFirst()){
            do {
                if ((un.equals(cursor.getString(4))) && (pw.equals(cursor.getString(5)))) {
                    startActivity(new Intent(getApplicationContext(),Menu.class));
                }
            } while (cursor.moveToNext());
        }
        dbUtente.close();

        //Assicurarsi che il pulsante di registrazione si blocchi dopo la prima registrazione perchè
        //l'Activity in cui vado dopo il login è generica per qualsiasi utente loggato

    }

    public void onClick_reg(View view){

        // Da discutere la possibilità di azzerare il database ogni volta che si preme il pulsante registrazione
        // anche perchè questo comportarebbe alla creazione di un nuovo utente e sempre utile durante il testing

        DBAccess dbAccess = DBAccess.getInstance(getApplicationContext());
        dbAccess.CancelCareer();

        //provvisoriamente ho settato il metodo startActivity per indirizzarmi alla
        //SecondRegisterActivity invece che alla FirstRegisterActivity per evitare
        //di registrare un nuovo utente nel Database Utente ad ogni prova dell'app
        startActivity(new Intent(getApplicationContext(), SecondRegistrationActivity.class));
    }
}