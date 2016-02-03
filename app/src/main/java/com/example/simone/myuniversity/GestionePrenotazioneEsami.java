package com.example.simone.myuniversity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class GestionePrenotazioneEsami extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestione_prenotazione_esami);
    }

    public void onClick_aggPren(View view){
        startActivity(new Intent(getApplicationContext(), GestionePrenotazioneEsamiAggiungi.class));
    }

    public void onClick_modPren(View view){
        startActivity(new Intent(getApplicationContext(), GestionePrenotazioneEsamiModifica.class));
    }

    public void onClick_cancPren(View view){
        startActivity(new Intent(getApplicationContext(), GestionePrenotazioneEsamiElimina.class));
    }

    public void onClick_endPren(View view){
        finish();
    }
}
