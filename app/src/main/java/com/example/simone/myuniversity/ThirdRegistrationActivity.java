package com.example.simone.myuniversity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

public class ThirdRegistrationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third_registration);
        ListView listview = (ListView) findViewById(R.id.listPSfinale);

        DBAccess databaseAccess = DBAccess.getInstance(this);
        databaseAccess.open();
        List<String> seguiti = databaseAccess.getSeguiti();
        databaseAccess.close();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, seguiti);
        listview.setAdapter(adapter);
        listview.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

    }

    public void onClick_back3(View view){
        startActivity(new Intent(this, SecondRegistrationActivity.class));
        DBAccess dbAccess = DBAccess.getInstance(getBaseContext());
        dbAccess.open();
        dbAccess.setSeguitiToZero();
        dbAccess.close();
    }



    // Matti
}
