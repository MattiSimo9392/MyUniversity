package com.example.simone.myuniversity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;

import java.util.List;

public class GestionePrenotazioneEsamiAggiungi extends AppCompatActivity {

    ListView listview;
    Cursor cursor1, cursor2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestione_prenotazione_esami_aggiungi);

        listview = (ListView) findViewById(R.id.lv_prenExam);

        DBAccess databaseAccess = DBAccess.getInstance(this);
        databaseAccess.open();
        List<String> esamiPrenotabili = databaseAccess.getDaSeguire();
        databaseAccess.close();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, esamiPrenotabili);
        listview.setAdapter(adapter);
        listview.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        // correggere tutta questa parte
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                final String insegnamentoSelezionato = listview.getItemAtPosition(position).toString();

                View dialog_view = (LayoutInflater.from(GestionePrenotazioneEsamiAggiungi.this)).inflate(R.layout.prenota_esame, null);
                AlertDialog.Builder prenota = new AlertDialog.Builder(GestionePrenotazioneEsamiAggiungi.this);
                prenota.setView(dialog_view);

                final CheckBox appello1 = (CheckBox) dialog_view.findViewById(R.id.cbx_appello1);
                final CheckBox appello2 = (CheckBox) dialog_view.findViewById(R.id.cbx_appello2);

                DBAccess dbAccess = DBAccess.getInstance(getApplicationContext());
                dbAccess.open();
                cursor1 = dbAccess.getAppello1(insegnamentoSelezionato);
                cursor1.moveToFirst();
                cursor2 = dbAccess.getAppello2(insegnamentoSelezionato);
                cursor2.moveToFirst();
                dbAccess.close();

                appello1.setText(cursor1.getString(cursor1.getColumnIndex("Data_1")) + "\t\t" + cursor1.getString(cursor1.getColumnIndex("Aula_1")) + "\t\t" + cursor1.getString(cursor1.getColumnIndex("Ora_1")));
                appello2.setText(cursor2.getString(cursor2.getColumnIndex("Data_2")) + "\t\t" + cursor2.getString(cursor2.getColumnIndex("Aula_2")) + "\t\t" + cursor2.getString(cursor2.getColumnIndex("Ora_2")));

                prenota.setTitle(insegnamentoSelezionato);
                prenota.setCancelable(true);
                prenota.setNegativeButton("Annulla", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                prenota.setPositiveButton("Conferma", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //DBAccess dbAccess = DBAccess.getInstance(getApplicationContext());
                        //dbAccess.open();
                        // inserire la query per aggiungere la data al db
                        //dbAccess.close();

                        dialog.dismiss();


                        //finish();
                        //startActivity(new Intent(getApplicationContext(), GestionePrenotazioneEsamiAggiungi.class));

                    }
                });
                Dialog prenotaDialog = prenota.create();
                prenotaDialog.show();
            }
        });
        //
    }
}
