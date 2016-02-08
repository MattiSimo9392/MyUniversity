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
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

public class VisualizzaOrariLezioni extends AppCompatActivity {

    ListView listview;
    Cursor lunLez, lunEx, marLez, marEx, merLez, merEx, gioLez, gioEx, venLez, venEx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualizza_orari_lezioni);

        listview = (ListView) findViewById(R.id.lv_showOrari);

        final DBAccess databaseAccess = DBAccess.getInstance(this);
        databaseAccess.open();
        List<String> esamiSeguiti = databaseAccess.getSeguiti();
        databaseAccess.close();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, esamiSeguiti);
        listview.setAdapter(adapter);
        listview.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                final String insegnamentoSelezionato = listview.getItemAtPosition(position).toString();

                View dialog_view = (LayoutInflater.from(VisualizzaOrariLezioni.this)).inflate(R.layout.dettagli_orario_lezioni, null);
                AlertDialog.Builder showOrari = new AlertDialog.Builder(VisualizzaOrariLezioni.this);
                showOrari.setView(dialog_view);

                final TextView lun = (TextView)dialog_view.findViewById(R.id.tv_lun_2);
                final TextView mar = (TextView)dialog_view.findViewById(R.id.tv_mar_2);
                final TextView mer = (TextView)dialog_view.findViewById(R.id.tv_mer_2);
                final TextView gio = (TextView)dialog_view.findViewById(R.id.tv_gio_2);
                final TextView ven = (TextView)dialog_view.findViewById(R.id.tv_ven_2);

                databaseAccess.open();
                lunLez = databaseAccess.getOrarioLun(insegnamentoSelezionato);
                lunLez.moveToFirst();
                lunEx = databaseAccess.getOrarioEserLun(insegnamentoSelezionato);
                lunEx.moveToFirst();
                marLez = databaseAccess.getOrarioMar(insegnamentoSelezionato);
                marLez.moveToFirst();
                marEx = databaseAccess.getOrarioEserMar(insegnamentoSelezionato);
                marEx.moveToFirst();
                merLez = databaseAccess.getOrarioMer(insegnamentoSelezionato);
                merLez.moveToFirst();
                merEx = databaseAccess.getOrarioEserMer(insegnamentoSelezionato);
                merEx.moveToFirst();
                gioLez = databaseAccess.getOrarioGio(insegnamentoSelezionato);
                gioLez.moveToFirst();
                gioEx = databaseAccess.getOrarioEserGio(insegnamentoSelezionato);
                gioEx.moveToFirst();
                venLez = databaseAccess.getOrarioVen(insegnamentoSelezionato);
                venLez.moveToFirst();
                venEx = databaseAccess.getOrarioEserVen(insegnamentoSelezionato);
                venEx.moveToFirst();
                databaseAccess.close();

                showOrari.setTitle(insegnamentoSelezionato);
                showOrari.setCancelable(true);

                showOrari.setPositiveButton("Chiudi", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                Dialog showOrariDialog = showOrari.create();
                showOrariDialog.show();
            }
        });
    }

    public void onClick_showOrari_end(View view){
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
