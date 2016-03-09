package com.example.simone.myuniversity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

public class GestionePrenotazioneEsamiModifica extends AppCompatActivity {

    ListView listview;
    Cursor cursor1, cursor2, cursorPren;
    String datapren, data1, data2, messaggio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestione_prenotazione_esami_modifica);

        listview = (ListView) findViewById(R.id.lv_modExam);

        DBAccess databaseAccess = DBAccess.getInstance(this);
        databaseAccess.open();
        List<String> esamiPrenotati_mod = databaseAccess.getPrenotati();
        databaseAccess.close();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, esamiPrenotati_mod);
        listview.setAdapter(adapter);
        listview.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                final String insegnamentoSelezionato = listview.getItemAtPosition(position).toString();

                final DBAccess dbAccess = DBAccess.getInstance(getApplicationContext());
                dbAccess.open();
                cursor1 = dbAccess.getAppello1(insegnamentoSelezionato);
                cursor1.moveToFirst();
                cursor2 = dbAccess.getAppello2(insegnamentoSelezionato);
                cursor2.moveToFirst();
                cursorPren = dbAccess.getDataEsameSuperato(insegnamentoSelezionato);
                cursorPren.moveToFirst();
                dbAccess.close();

                data1 = cursor1.getString(cursor1.getColumnIndex("Data_1"));
                data2 = cursor2.getString(cursor2.getColumnIndex("Data_2"));
                datapren = cursorPren.getString(cursorPren.getColumnIndex("DataSuperamento"));

                if (datapren.equals(data1)) {

                    messaggio = "E' disponibile un altro appello il " + data2 + " alle ore " + cursor2.getString(cursor2.getColumnIndex("Ora_2")) + " in Aula " + cursor2.getString(cursor2.getColumnIndex("Aula_2")) + ". Vuoi modificare la Prenotazione?";
                } else if (datapren.equals(data2)) {

                    messaggio = "Non ci sono altri appelli disponibili per questo esame. Se decidi di non sostenerlo annulla la prenotazione nella sezione \"Elimina Prenotazione a un Esame\"";
                }

                AlertDialog.Builder prenota_mod = new AlertDialog.Builder(GestionePrenotazioneEsamiModifica.this);
                prenota_mod.setTitle(insegnamentoSelezionato);
                prenota_mod.setMessage(messaggio);
                prenota_mod.setCancelable(true);
                prenota_mod.setNegativeButton("Annulla", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                prenota_mod.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        //manca la cancellazione della data dal db
                        dbAccess.open();
                        dbAccess.setDataEsameSuperato(insegnamentoSelezionato, data2);
                        dbAccess.close();
                        finish();
                        startActivity(new Intent(getApplicationContext(), GestionePrenotazioneEsamiModifica.class));
                    }
                });
                Dialog modPrenotaDialog = prenota_mod.create();
                modPrenotaDialog.show();
            }
        });
    }

    public void onClick_modExam_end(View view){
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
