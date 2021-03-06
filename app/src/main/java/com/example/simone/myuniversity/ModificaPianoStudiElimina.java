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
import android.widget.TextView;

import java.util.List;

//Activity per l'eliminazione di un corso dal Piano Di Studi
public class ModificaPianoStudiElimina extends AppCompatActivity {

    ListView listview;
    TextView listaVuota;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modifica_piano_studi_elimina);

        listview = (ListView) findViewById(R.id.lv_modPS_del);

        DBAccess databaseAccess = DBAccess.getInstance(this);
        databaseAccess.open();
        List<String> nelPiano = databaseAccess.getNelPiano();
        databaseAccess.close();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, nelPiano);
        listview.setAdapter(adapter);
        listview.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        listview.setEmptyView(findViewById(R.id.tv_modPS_del_4));

        listaVuota = (TextView)findViewById(R.id.tv_modPS_del_4);

        listaVuota.setText("Piano di Studi Vuoto");

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                final String insegnamentoSelezionato = listview.getItemAtPosition(position).toString();

                AlertDialog.Builder delete = new AlertDialog.Builder(ModificaPianoStudiElimina.this);
                delete.setTitle(insegnamentoSelezionato);
                delete.setMessage("Sei sicuro di voler eliminare " + insegnamentoSelezionato + " dal tuo Piano Di Studi?");
                delete.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                delete.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DBAccess dbAccess = DBAccess.getInstance(getApplicationContext());
                        dbAccess.open();
                        dbAccess.setNelPianoNull(insegnamentoSelezionato);
                        dbAccess.setVotoNull(insegnamentoSelezionato);
                        dbAccess.setDataNull(insegnamentoSelezionato);
                        dbAccess.setSeguitiNull(insegnamentoSelezionato);
                        dbAccess.close();


                        finish();
                        startActivity(new Intent(getApplicationContext(), ModificaPianoStudiElimina.class));

                    }
                });
                Dialog deleteDialog = delete.create();
                deleteDialog.show();
            }
        });
    }

    public void onClick_cancExam_fine(View view){
        startActivity(new Intent(getApplicationContext(), ModificaPianoStudi.class));
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
