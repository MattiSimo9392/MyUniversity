package com.example.simone.myuniversity;

import android.app.ListActivity;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

public class VisualizzaCarriera extends ListActivity {

    ListView listview;
    Cursor cursorCFUpassed;
    int intCFUpassed;
    Cursor cursorCFUTotal;
    int intCFUTotal;
    Cursor cursorExamsPassed;
    int intExamsPassed;
    Cursor cursorExamsLost;
    int intExamsLost;
    Cursor cursorStudente;
    Cursor listaEsami;
    String Studente;
    String Matricola;

    long id ;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualizza_carriera);

        // inizializzo le TextView

        TextView textView_Utente = (TextView)findViewById(R.id.Studente);
        TextView textView_Matricola = (TextView)findViewById(R.id.Matricola);
        TextView textView_CFU = (TextView)findViewById(R.id.tv_CFU);
        TextView textView_ExamsPassed = (TextView)findViewById(R.id.EsamiPassati);
        TextView textView_ExamsLost = (TextView)findViewById(R.id.tv_EsamiMancanti);

        listview = (ListView) findViewById(android.R.id.list);

        // Apro il database
        DBAccess database = DBAccess.getInstance(getApplicationContext());
        database.open();
        // mi ricavo i cursor con cui andrò a completare le text view
        listaEsami = database.cursorExamsPassed();

        startManagingCursor(listaEsami);

        CarrieraCursorAdapter adapter = new CarrieraCursorAdapter(getApplicationContext() , listaEsami );
        setListAdapter(adapter);

        cursorCFUpassed = database.getCFUpassed();
        cursorCFUTotal = database.getCFUTotal();
        cursorExamsPassed = database.countCFUPassed();
        cursorExamsLost = database.countExamsLost();

        // li converto
        intCFUpassed = cursorCFUpassed.getInt(0);
        intCFUTotal = cursorCFUTotal.getInt(0);
        intExamsPassed = cursorExamsPassed.getInt(0);
        intExamsLost = cursorExamsLost.getInt(0);

        //e aggiungo i testi

        textView_CFU.setText("CFU : " + intCFUpassed + "/" + intCFUTotal);
        textView_ExamsPassed.setText("Esami Passati : " + intExamsPassed);
        textView_ExamsLost.setText("Esami Mancanti : " + intExamsLost);

        // chiudo il database
        database.close();

        //Per l'utente apro il GestioneDBUtente

        GestioneDBUtente dbUtente = new GestioneDBUtente(this);
        dbUtente.open();

        // Mi ricavo dalla shared preferences l'id
        sharedPreferences = getSharedPreferences("_IdLog", MODE_PRIVATE);
        id = sharedPreferences.getLong("_IdLog", -1);

        //Vado a prendere Nome , cognome e matricola
        cursorStudente = dbUtente.get_User(id);
        Studente = cursorStudente.getString(1) + "    " + cursorStudente.getString(2);
        Matricola = cursorStudente.getString(3);

        textView_Utente.setText(Studente);
        textView_Matricola.setText(Matricola);

        dbUtente.close();
    }
}
