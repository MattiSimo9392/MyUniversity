package com.example.simone.myuniversity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

public class FifthRegistrationActivity extends AppCompatActivity {

    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fifth_registration);

        listView = (ListView) findViewById(R.id.listPSfinale3);

        DBAccess databaseAccess = DBAccess.getInstance(this);
        databaseAccess.open();
        List<String> seguiti = databaseAccess.getDaSeguire();
        databaseAccess.close();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_multiple_choice, seguiti);
        listView.setAdapter(adapter);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        // Aggiunta check and uncheck della lista

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(listView.isItemChecked(position)){
                    String listString = listView.getItemAtPosition(position).toString();
                    Toast.makeText(getBaseContext(), "Hai selezionato : " + listString, Toast.LENGTH_SHORT).show();
                    DBAccess dbAccess = DBAccess.getInstance(getBaseContext());
                    dbAccess.open();
                    dbAccess.setSeguiti(listString);
                    dbAccess.close();

                }
                else{
                    String listDeselected = listView.getItemAtPosition(position).toString();
                    Toast.makeText(getBaseContext(), "Hai deselezionato : " + listDeselected , Toast.LENGTH_SHORT).show();
                    DBAccess dbAccess = DBAccess.getInstance(getBaseContext());
                    dbAccess.open();
                    dbAccess.setSeguitiNull(listDeselected);
                    dbAccess.close();

                }

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
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("LOGOUT", true);
                startActivity(intent);
            }
        });
        Dialog exitDialog = exit.create();
        exitDialog.show();
    }

    public void onClick_back5(View view){
        DBAccess database = DBAccess.getInstance(getBaseContext());
        database.open();
        database.setSeguitiToZero();
        database.close();
        startActivity(new Intent(getApplicationContext(), FourthRegistrationActivity.class));
    }

    public void onClick_continue5(View view){
        // viene eliminato il pulsante registrati
        /*
        SharedPreferences mprefs = getSharedPreferences("Registrazione" , MODE_PRIVATE);
        SharedPreferences.Editor editor = mprefs.edit();
        editor.putString("Registrazione" , "2");
        editor.apply();
        */
        startActivity(new Intent (getApplicationContext() , MainActivity.class));
    }
}

