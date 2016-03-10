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
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class GestionePrenotazioneEsamiElimina extends AppCompatActivity {

    ListView listview;
    Cursor cursor1, cursor2, cursorPren;
    String datapren, data1, data2, messaggio;
    TextView listaVuota;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestione_prenotazione_esami_elimina);

        listview = (ListView) findViewById(R.id.lv_cancExam);

        DBAccess databaseAccess = DBAccess.getInstance(this);
        databaseAccess.open();
        List<String> esamiPrenotati_canc = databaseAccess.getPrenotati();
        databaseAccess.close();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, esamiPrenotati_canc);
        listview.setAdapter(adapter);
        listview.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        listview.setEmptyView(findViewById(R.id.tv_cancExam_4));

        listaVuota = (TextView)findViewById(R.id.tv_cancExam_4);

        listaVuota.setText("Nessun Esame Prenotato");

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

                if (datapren.equals(data1)){

                    messaggio = "Sei sicuro di voler annullare la prenotazione per " + insegnamentoSelezionato + " del " + data1 + " in Aula " + cursor1.getString(cursor1.getColumnIndex("Aula_1")) + " alle Ore " + cursor1.getString(cursor1.getColumnIndex("Ora_1")) + "?";
                }
                else if (datapren.equals(data2)){

                    messaggio = "Sei sicuro di voler annullare la prenotazione per " + insegnamentoSelezionato + " del " + data2 + " in Aula " + cursor2.getString(cursor2.getColumnIndex("Aula_2")) + " alle Ore " + cursor2.getString(cursor2.getColumnIndex("Ora_2")) + "?";
                }

                AlertDialog.Builder prenota_canc = new AlertDialog.Builder(GestionePrenotazioneEsamiElimina.this);
                prenota_canc.setTitle(insegnamentoSelezionato);
                prenota_canc.setMessage(messaggio);
                prenota_canc.setCancelable(true);
                prenota_canc.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                prenota_canc.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        //manca la cancellazione della data dal db
                        dbAccess.open();
                        dbAccess.setDataNull(insegnamentoSelezionato);
                        dbAccess.close();
                        finish();
                        startActivity(new Intent(getApplicationContext(), GestionePrenotazioneEsamiElimina.class));
                    }
                });
                Dialog sPrenotaDialog = prenota_canc.create();
                sPrenotaDialog.show();
            }
        });
    }

    public void onClick_cancExam_end(View view){
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
