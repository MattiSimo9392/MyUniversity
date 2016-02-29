package com.example.simone.myuniversity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.provider.CalendarContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class GestionePrenotazioneEsamiAggiungi extends AppCompatActivity {

    ListView listview;
    Cursor cursor1, cursor2;
    Menu.Data dataPA , dataSA;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestione_prenotazione_esami_aggiungi);

        listview = (ListView) findViewById(R.id.lv_prenExam);

        DBAccess databaseAccess = DBAccess.getInstance(this);
        databaseAccess.open();
        List<String> esamiPrenotabili = databaseAccess.getPrenotabili();
        databaseAccess.close();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, esamiPrenotabili);
        listview.setAdapter(adapter);
        listview.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

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

                ///////////////////////////////Tentativo di aggiunta un controllo data sugli appelli ////////////////////
                dataPA = new Menu().new Data();
                dataPA.DateSplitterV2(cursor1.getString(cursor1.getColumnIndex("Data_1")));
                Calendar data1 = Calendar.getInstance();
                data1.set(dataPA.year, dataPA.month - 1, dataPA.day);
                dataSA = new Menu().new Data();
                dataSA.DateSplitterV2(cursor2.getString(cursor2.getColumnIndex("Data_2")));
                Calendar data2 = Calendar.getInstance();
                data2.set(dataSA.year, dataSA.month - 1, dataSA.day);

                final Calendar currentDate = Calendar.getInstance();
                currentDate.setTime(new Date());

                if (data1.after(currentDate)) {                 // primo appello è > datacorrente
                    appello1.setText("Appello del " + cursor1.getString(cursor1.getColumnIndex("Data_1")) + " in Aula " + cursor1.getString(cursor1.getColumnIndex("Aula_1")) + " alle " + cursor1.getString(cursor1.getColumnIndex("Ora_1")));
                    appello2.setText("Appello del " + cursor2.getString(cursor2.getColumnIndex("Data_2")) + " in Aula " + cursor2.getString(cursor2.getColumnIndex("Aula_2")) + " alle " + cursor2.getString(cursor2.getColumnIndex("Ora_2")));
                }else if (!data1.after(currentDate) && data2.after(currentDate) ){   // primo appello < datacorrente mentre secondo appello > corrente
                    appello1.setVisibility(View.INVISIBLE);
                    appello2.setText("Appello del " + cursor2.getString(cursor2.getColumnIndex("Data_2")) + " in Aula " + cursor2.getString(cursor2.getColumnIndex("Aula_2")) + " alle " + cursor2.getString(cursor2.getColumnIndex("Ora_2")));
                }else if(!data2.after(currentDate)){            // secondo appello < datacorrente
                    appello1.setVisibility(View.INVISIBLE);
                    appello2.setVisibility(View.INVISIBLE);
                }

                ///// Fine tentativo (Testato e funzionante , ma da ritestare qualche volta in più che non guasta )

                appello1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        appello2.setChecked(false);
                    }
                });

                appello2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        appello1.setChecked(false);
                    }
                });

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

                        DBAccess dbAccess = DBAccess.getInstance(getApplicationContext());
                        dbAccess.open();

                        if (appello1.isChecked()) {

                            Toast.makeText(getApplicationContext(), "Hai prenotato: " + appello1.getText().toString(), Toast.LENGTH_SHORT).show();
                            //inserire la query per aggiungere la data al db
                            dbAccess.setDataEsameSuperato(insegnamentoSelezionato, cursor1.getString(cursor1.getColumnIndex("Data_1")));
                        } else if (appello2.isChecked()) {

                            Toast.makeText(getApplicationContext(), "Hai prenotato: " + appello2.getText().toString(), Toast.LENGTH_SHORT).show();
                            //inserire la query per aggiungere la data al db
                            dbAccess.setDataEsameSuperato(insegnamentoSelezionato, cursor2.getString(cursor2.getColumnIndex("Data_2")));
                        } else {

                            Toast.makeText(getApplicationContext(), "Non hai selezionato nessun appello! Esame non prenotato", Toast.LENGTH_SHORT).show();
                        }

                        dbAccess.close();

                        finish();
                        startActivity(new Intent(getApplicationContext(), GestionePrenotazioneEsamiAggiungi.class));

                    }
                });
                Dialog prenotaDialog = prenota.create();
                prenotaDialog.show();
            }
        });

    }

    public void onClick_prenExam_end(View view){
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
