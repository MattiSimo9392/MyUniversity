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
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class MainActivity extends Activity {

    EditText et_username, et_password;
    GestioneDBUtente dbUtente;
    Cursor cursor;

    SharedPreferences sharedPreferences;
    long id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button registration = (Button)findViewById(R.id.btn_registrati);

        et_username = (EditText) findViewById(R.id.et_usernameLog);
        et_password = (EditText) findViewById(R.id.et_passwordLog);
        dbUtente = new GestioneDBUtente(getApplicationContext());

        sharedPreferences = getSharedPreferences("_IdLog", MODE_PRIVATE);
        id = sharedPreferences.getLong("_IdLog", -1);

        TextView recuperoPass = (TextView) findViewById(R.id.recoverPassword);
        recuperoPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View dialog_view = (LayoutInflater.from(MainActivity.this)).inflate(R.layout.recover, null);
                AlertDialog.Builder insert = new AlertDialog.Builder(MainActivity.this);
                insert.setView(dialog_view);

                final EditText ed_UserRec = (EditText)dialog_view.findViewById(R.id.et_RecoverUser);
                final EditText ed_MatRec = (EditText)dialog_view.findViewById(R.id.et_RecoverMatr);

                insert.setTitle("Hai dimanticato la Password?");
                insert.setMessage("Inserisci Username e Matricola per poter recuperare la Password");

                insert.setNegativeButton("Annulla", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                insert.setPositiveButton("Visualizza Password", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String UserRec = ed_UserRec.getText().toString();
                        String MatrRec = ed_MatRec.getText().toString();
                        dbUtente.open();
                        cursor = dbUtente.get_User(id);
                        if ((UserRec.equals(cursor.getString(4))) && (MatrRec.equals(cursor.getString(3)))) {
                            Toast.makeText(getApplicationContext(), "Password: " + cursor.getString(5), Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "Username e/o Matricola Errati, Impossibile recuperare la Password!", Toast.LENGTH_LONG).show();
                        }
                        dbUtente.close();
                    }
                });
                Dialog dialog = insert.create();
                dialog.show();
            }
        });

        //Questa parte di codice viene chiamata quando si preme il pulsante indietro e si conferma
        //di voler chiudere l'app
        if (getIntent().getBooleanExtra("LOGOUT", false))
        {
            finish();
        }

        // impostazione preferences con la FifthRegistrationActivity

        SharedPreferences mprefs = getSharedPreferences("Registrazione" , MODE_PRIVATE);
        String string = mprefs.getString("Registrazione" , "");
        if(string.equals("2")){registration.setVisibility(View.GONE);}
        else{registration.setVisibility(View.VISIBLE);}

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

    public void onClick_login(View view){

        String un = et_username.getText().toString();
        String pw = et_password.getText().toString();

        dbUtente.open();

        cursor = dbUtente.get_User(id);
        if (cursor.getCount() != 0) {
            if ((un.equals(cursor.getString(4))) && (pw.equals(cursor.getString(5)))) {
                startActivity(new Intent(getApplicationContext(), Menu.class));
            } else {
                Toast.makeText(getApplicationContext(), "Username e/o Password inseriti non Corretti! Riprova!", Toast.LENGTH_SHORT).show();
            }
        }
        else {
            Toast.makeText(getApplicationContext(), "Username e/o Password inseriti non Corretti! Riprova!", Toast.LENGTH_SHORT).show();
        }
        dbUtente.close();

        //Assicurarsi che il pulsante di registrazione si blocchi dopo la prima registrazione perchè
        //l'Activity in cui vado dopo il login è generica per qualsiasi utente loggato

    }

    public void onClick_reg(View view){

        // Da discutere la possibilità di azzerare il database ogni volta che si preme il pulsante registrazione
        // anche perchè questo comportarebbe alla creazione di un nuovo utente e sempre utile durante il testing

        DBAccess dbAccess = DBAccess.getInstance(getApplicationContext());
        dbAccess.open();
        dbAccess.CancelCareer();
        dbAccess.close();

        startActivity(new Intent(getApplicationContext(), FirstRegistrationActivity.class));
    }

    public void onClick_recuperaPsw (View view){

        View dialog_view = (LayoutInflater.from(MainActivity.this)).inflate(R.layout.recover, null);
        AlertDialog.Builder insert = new AlertDialog.Builder(MainActivity.this);
        insert.setView(dialog_view);

        final EditText ed_UserRec = (EditText)dialog_view.findViewById(R.id.et_RecoverUser);
        final EditText ed_MatRec = (EditText)dialog_view.findViewById(R.id.et_RecoverMatr);

        insert.setTitle("Hai dimanticato la Password?");
        insert.setMessage("Inserisci Username e Matricola per poter recuperare la Password");

        insert.setNegativeButton("Annulla", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        insert.setPositiveButton("Visualizza Password", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String UserRec = ed_UserRec.getText().toString();
                String MatrRec = ed_MatRec.getText().toString();
                dbUtente.open();
                cursor = dbUtente.get_User(id);
                if ((UserRec.equals(cursor.getString(4))) && (MatrRec.equals(cursor.getString(3)))) {
                    Toast.makeText(getApplicationContext(), "Password: " + cursor.getString(5), Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Username e/o Matricola Errati, Impossibile recuperare la Password!", Toast.LENGTH_LONG).show();
                }
                dbUtente.close();
            }
        });
        Dialog dialog = insert.create();
        dialog.show();
    }
}