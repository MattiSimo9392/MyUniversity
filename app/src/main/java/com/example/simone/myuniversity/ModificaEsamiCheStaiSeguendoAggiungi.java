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
import android.widget.Toast;

import java.util.List;

public class ModificaEsamiCheStaiSeguendoAggiungi extends AppCompatActivity {

    ListView listview;
    TextView listaVuota;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modifica_esami_che_stai_seguendo_aggiungi);

        listview = (ListView) findViewById(R.id.lv_modSeg_agg);

        DBAccess databaseAccess = DBAccess.getInstance(this);
        databaseAccess.open();
        List<String> nonSeguiti = databaseAccess.getNonSeguiti();
        databaseAccess.close();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_multiple_choice, nonSeguiti);
        listview.setAdapter(adapter);
        listview.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        listview.setEmptyView(findViewById(R.id.tv_modSeg_agg_4));

        listaVuota = (TextView)findViewById(R.id.tv_modSeg_agg_4);

        listaVuota.setText("Nessun Insegnamento Seguibile");

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (listview.isItemChecked(position)) {
                    String listString = listview.getItemAtPosition(position).toString();
                    Toast.makeText(getApplicationContext(), "Hai selezionato: " + listString, Toast.LENGTH_SHORT).show();

                    DBAccess dbAccess = DBAccess.getInstance(getApplicationContext());
                    dbAccess.open();
                    dbAccess.setSeguiti(listString);
                    dbAccess.close();

                } else {
                    String listDeselected = listview.getItemAtPosition(position).toString();
                    Toast.makeText(getBaseContext(), "Hai deselezionato: " + listDeselected, Toast.LENGTH_SHORT).show();

                    DBAccess dbAccess = DBAccess.getInstance(getBaseContext());
                    dbAccess.open();
                    dbAccess.setSeguitiNull(listDeselected);
                    dbAccess.close();
                }
            }
        });
    }

    public void onClick_aggExamSeg_fine(View view){
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
