package com.example.simone.myuniversity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

public class ModificaPianoStudiAggiungi extends AppCompatActivity {

    ListView listview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modifica_piano_studi_aggiungi);

        listview = (ListView) findViewById(R.id.lv_modPS_agg);

        DBAccess databaseAccess = DBAccess.getInstance(this);
        databaseAccess.open();
        List<String> nonNelPiano = databaseAccess.getNonNelPiano();
        databaseAccess.close();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_multiple_choice, nonNelPiano);
        listview.setAdapter(adapter);
        listview.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if(listview.isItemChecked(position)){
                    String listString = listview.getItemAtPosition(position).toString();
                    Toast.makeText(getApplicationContext(), "Hai selezionato: " + listString, Toast.LENGTH_SHORT).show();

                    DBAccess dbAccess = DBAccess.getInstance(getApplicationContext());
                    dbAccess.open();
                    dbAccess.setNelPiano(listString);
                    dbAccess.close();

                }
                else{
                    String listDeselected = listview.getItemAtPosition(position).toString();
                    Toast.makeText(getBaseContext(), "Hai deselezionato: " + listDeselected , Toast.LENGTH_SHORT).show();

                    DBAccess dbAccess = DBAccess.getInstance(getBaseContext());
                    dbAccess.open();
                    dbAccess.setNelPianoNull(listDeselected);
                    dbAccess.close();
                }
            }
        });
    }

    public void onClick_aggExam_fine(View view){
        finish();
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
