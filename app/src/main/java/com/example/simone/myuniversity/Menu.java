package com.example.simone.myuniversity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

public class Menu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        Button cancelCareer = (Button) findViewById(R.id.cancelCareer);

    }

    public void onClick_CancelCareer(){
        // Riportiamo la preference al valore iniziale in modo che compaia il bottone
        /*
        SharedPreferences mprefs = getSharedPreferences("Registrazione" , MODE_PRIVATE);
        SharedPreferences.Editor editor = mprefs.edit();
        editor.putString("Registrazione" , "3");            // condizione per la ricomparsa del pulsante
        editor.apply();
        */

        AlertDialog.Builder insert = new AlertDialog.Builder(Menu.this);
        insert.setMessage("Sei Sicuro di voler eliminare la tua carriera ? \n l'operazione è Irreversibile !");
        insert.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DBAccess database = DBAccess.getInstance(getBaseContext());
                database.CancelCareer();
                startActivity(new Intent(getBaseContext(), MainActivity.class));
            }
        });
        insert.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
            }
        });
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
