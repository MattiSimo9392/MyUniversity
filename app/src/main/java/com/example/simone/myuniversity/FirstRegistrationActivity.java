package com.example.simone.myuniversity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class FirstRegistrationActivity extends Activity {

    EditText et_nome, et_cognome, et_matricola, et_username, et_password;
    GestioneDBUtente dbUtente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_registration);

        et_nome = (EditText)findViewById(R.id.et_nome);
        et_cognome = (EditText)findViewById(R.id.et_cognome);
        et_matricola = (EditText)findViewById(R.id.et_matricola);
        et_username = (EditText)findViewById(R.id.et_username);
        et_password = (EditText)findViewById(R.id.et_password);

        dbUtente = new GestioneDBUtente(getApplicationContext());
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

    public void onClick_continue(View view) {

        String nome = et_nome.getText().toString();
        String cognome = et_cognome.getText().toString();
        String matricola = et_matricola.getText().toString();
        String username = et_username.getText().toString();
        String password = et_password.getText().toString();

        if ((nome.equals("")) || (cognome.equals("")) || (matricola.equals("")) || (username.equals("")) || (password.equals(""))){

            AlertDialog.Builder ad = new AlertDialog.Builder(FirstRegistrationActivity.this);
            ad.setTitle("Registrazione non Riuscita! Dati Mancanti!");
            ad.setCancelable(false);
            ad.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    dialog.dismiss();
                }
            });
            Dialog dialog = ad.create();
            dialog.show();
        }

        else {

            dbUtente.open();
            dbUtente.insertUser(nome, cognome, matricola, username, password);
            dbUtente.close();

            startActivity(new Intent(getApplicationContext(), SecondRegistrationActivity.class));

        }
    }
}
