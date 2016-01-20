package com.example.simone.myuniversity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

public class FourthRegistrationActivity extends AppCompatActivity {

    ListView listView;
    String voto_esame, data_esame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fourth_registration);

        listView = (ListView) findViewById(R.id.listPSfinale2);

        DBAccess databaseAccess = DBAccess.getInstance(this);
        databaseAccess.open();
        List<String> seguiti = databaseAccess.getSeguiti();
        databaseAccess.close();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, seguiti);
        listView.setAdapter(adapter);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String insegnamentoSelezionato = listView.getItemAtPosition(position).toString();

                View dialog_view = (LayoutInflater.from(FourthRegistrationActivity.this)).inflate(R.layout.set_voto_data, null);
                AlertDialog.Builder insert = new AlertDialog.Builder(FourthRegistrationActivity.this);
                insert.setView(dialog_view);
                final EditText voto = (EditText) view.findViewById(R.id.et_voto);
                final EditText data = (EditText) view.findViewById(R.id.et_data);
                insert.setTitle(insegnamentoSelezionato);
                insert.setCancelable(true);
                insert.setNegativeButton("Annulla", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                    }
                });
                insert.setPositiveButton("Conferma", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //catturare il voto e la data dai rispettivi editText e salvare i valori nel DB
                        voto_esame = voto.getText().toString();
                        data_esame = data.getText().toString();
                        Toast.makeText(getApplicationContext(), "Voto: "+ voto_esame + "\nData: " + data_esame, Toast.LENGTH_LONG).show();
                    }
                });
                Dialog insertDialog = insert.create();
                insertDialog.show();
            }
        });
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
