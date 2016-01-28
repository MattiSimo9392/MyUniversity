package com.example.simone.myuniversity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

public class ModificaPianoStudi extends AppCompatActivity {

    ListView listview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modifica_piano_studi);
        listview = (ListView) findViewById(R.id.lv_modPS);

        DBAccess databaseAccess = DBAccess.getInstance(this);
        databaseAccess.open();
        List<String> nelPiano = databaseAccess.getNelPiano();
        databaseAccess.close();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, nelPiano);
        listview.setAdapter(adapter);
        listview.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
    }

    public void onClick_aggExam(View view){

        startActivity(new Intent(getApplicationContext(), ModificaPianoStudiAggiungi.class));

    }

    public void onClick_cancExam(View view){

        startActivity(new Intent(getApplicationContext(), ModificaPianoStudiElimina.class));

    }

    public void onClick_confCarr(View view){
        finish();
    }
}
