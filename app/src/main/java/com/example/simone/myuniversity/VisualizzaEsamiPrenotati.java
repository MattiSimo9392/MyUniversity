package com.example.simone.myuniversity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.List;

public class VisualizzaEsamiPrenotati extends AppCompatActivity {

    ListView listview;
    Cursor cursor1, cursor2, cursorPren;
    String datapren, data1, data2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualizza_esami_prenotati);

        listview = (ListView) findViewById(R.id.lv_showExam);

        final DBAccess databaseAccess = DBAccess.getInstance(this);
        databaseAccess.open();
        List<String> esamiPrenotati = databaseAccess.getPrenotati();
        databaseAccess.close();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, esamiPrenotati);
        listview.setAdapter(adapter);
        listview.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                final String insegnamentoSelezionato = listview.getItemAtPosition(position).toString();

                View dialog_view = (LayoutInflater.from(VisualizzaEsamiPrenotati.this)).inflate(R.layout.dettagli_esame_prenotato, null);
                AlertDialog.Builder showPren = new AlertDialog.Builder(VisualizzaEsamiPrenotati.this);
                showPren.setView(dialog_view);

                final TextView data = (TextView)dialog_view.findViewById(R.id.tv_data_2);
                final TextView orario = (TextView)dialog_view.findViewById(R.id.tv_ora_2);
                final TextView aula = (TextView)dialog_view.findViewById(R.id.tv_aula_2);

                databaseAccess.open();
                cursor1 = databaseAccess.getAppello1(insegnamentoSelezionato);
                cursor1.moveToFirst();
                cursor2 = databaseAccess.getAppello2(insegnamentoSelezionato);
                cursor2.moveToFirst();
                cursorPren = databaseAccess.getDataEsameSuperato(insegnamentoSelezionato);
                cursorPren.moveToFirst();
                databaseAccess.close();

                data1 = cursor1.getString(cursor1.getColumnIndex("Data_1"));
                data2 = cursor2.getString(cursor2.getColumnIndex("Data_2"));
                datapren = cursorPren.getString(cursorPren.getColumnIndex("DataSuperamento"));

                if (datapren.equals(data1)){

                    data.setText(data1);
                    orario.setText(cursor1.getString(cursor1.getColumnIndex("Ora_1")));
                    aula.setText(cursor1.getString(cursor1.getColumnIndex("Aula_1")));
                }
                else if (datapren.equals(data2)){

                    data.setText(data2);
                    orario.setText(cursor2.getString(cursor2.getColumnIndex("Ora_2")));
                    aula.setText(cursor2.getString(cursor2.getColumnIndex("Aula_2")));
                }

                //showPren.setMessage("Dettagli Appello Prenotato");
                showPren.setTitle(insegnamentoSelezionato);
                showPren.setCancelable(true);

                showPren.setPositiveButton("Chiudi", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                Dialog showPrenDialog = showPren.create();
                showPrenDialog.show();
            }
        });
    }

    public void onClick_showExam_end(View view){
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
