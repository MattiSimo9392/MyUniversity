package com.example.simone.myuniversity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class SecondRegistrationActivity extends AppCompatActivity {

    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_registration);

        listView = (ListView)findViewById(R.id.listview);
        final DatabaseAcces databaseAcces = DatabaseAcces.getInstance(this);
        databaseAcces.open();
        List<String> quotes = databaseAcces.getQuotes();
        databaseAcces.close();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_multiple_choice, quotes);
        listView.setAdapter(adapter);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        //La listView con tutti gli Insegnamenti della tabella PianoDiStudi viene caricata
        //però per ora nessun Insegnamento è selezionabile e nessuna operazione sul database
        //è effettuabile

        //[AGGIUNTA DI MATTI NEL 19/01/2016] tentativo di incudere la lista e selezionare le colonne del piano di studi

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(listView.isItemChecked(position)){
                    String listString = listView.getItemAtPosition(position).toString();
                    Toast.makeText(getBaseContext(), "hai selezionato : " + listString , Toast.LENGTH_LONG).show();
                    // QUI ANDREBBE INSERITA LA QUERY PER L'UPDATE CON 'SEGUITO' A 'SI'
                    // tentativo di query SQL Matti
                    DBAccess dbAccess = DBAccess.getInstance(getBaseContext());
                    dbAccess.open();
                    dbAccess.setSeguiti(listString);
                    dbAccess.close();

                }
                else{
                    String listDeselected = listView.getItemAtPosition(position).toString();
                    Toast.makeText(getBaseContext(), "hai deselezionato : " + listDeselected , Toast.LENGTH_LONG).show();
                    // QUI UNA QUERY CHE RIPONE SEGUITO A NO NEL CASO DI DESELEZIONE
                    DBAccess dbAccess = DBAccess.getInstance(getBaseContext());
                    dbAccess.open();
                    dbAccess.setSeguitiNull(listDeselected);
                    dbAccess.close();

                }

            }
        });

        //[FINE AGGIUNTA MATTI 19/01/2016]




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

    public void onClick_continue2 (View view){

        startActivity(new Intent(this, ThirdRegistrationActivity.class));
        //commento casuale

    }

    public void onClick_back2(){

        startActivity(new Intent(this, FirstRegistrationActivity.class));
        DBAccess dbAccess = DBAccess.getInstance(getBaseContext());
        dbAccess.open();
        dbAccess.setSeguitiToZero();
        dbAccess.close();
    }
}
