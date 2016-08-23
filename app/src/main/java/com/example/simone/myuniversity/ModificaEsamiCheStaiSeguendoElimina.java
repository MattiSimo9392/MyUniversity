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

//Activity per eliminare un corso dalla lista degli Esami Seguiti
public class ModificaEsamiCheStaiSeguendoElimina extends AppCompatActivity {

    ListView listview;
    TextView listaVuota;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modifica_esami_che_stai_seguendo_elimina);

        listview = (ListView) findViewById(R.id.lv_modSeg_del);

        DBAccess databaseAccess = DBAccess.getInstance(this);
        databaseAccess.open();
        List<String> seguiti = databaseAccess.getSeguiti();
        databaseAccess.close();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, seguiti);
        listview.setAdapter(adapter);
        listview.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        listview.setEmptyView(findViewById(R.id.tv_modSeg_del_4));

        listaVuota = (TextView)findViewById(R.id.tv_modSeg_del_4);

        listaVuota.setText("Nessun Insegnamento Seguito");

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                final String insegnamentoSelezionato = listview.getItemAtPosition(position).toString();

                AlertDialog.Builder delete = new AlertDialog.Builder(ModificaEsamiCheStaiSeguendoElimina.this);
                delete.setTitle(insegnamentoSelezionato);
                delete.setMessage("Sei sicuro di voler smettere di seguire " + insegnamentoSelezionato + "?");
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
                        dbAccess.setSeguitiNull(insegnamentoSelezionato);
                        dbAccess.close();


                        finish();
                        startActivity(new Intent(getApplicationContext(), ModificaEsamiCheStaiSeguendoElimina.class));

                    }
                });
                Dialog deleteDialog = delete.create();
                deleteDialog.show();
            }
        });
    }

    public void onClick_cancExamSeg_fine(View view){
        startActivity(new Intent(getApplicationContext(), ModificaEsamiCheStaiSeguendo.class));
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
