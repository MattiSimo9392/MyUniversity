package com.example.simone.myuniversity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
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
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class FourthRegistrationActivity extends AppCompatActivity {

    ListView listView;
    public int voto_esame;
    public String data_esame;
    DatePickerDialog datePickerDialog;
    SimpleDateFormat dateFormat;
    Cursor cursor_voto, cursor_data;

    Boolean datacorretta;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fourth_registration);

        listView = (ListView) findViewById(R.id.listPSfinale2);

        DBAccess databaseAccess = DBAccess.getInstance(this);
        databaseAccess.open();
        List<String> seguiti = databaseAccess.getNelPiano();
        databaseAccess.close();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, seguiti);
        listView.setAdapter(adapter);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ITALY);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                final String insegnamentoSelezionato = listView.getItemAtPosition(position).toString();

                View dialog_view = (LayoutInflater.from(FourthRegistrationActivity.this)).inflate(R.layout.set_voto_data, null);
                AlertDialog.Builder insert = new AlertDialog.Builder(FourthRegistrationActivity.this);
                insert.setView(dialog_view);
                final EditText voto = (EditText) dialog_view.findViewById(R.id.et_voto);
                final EditText data = (EditText) dialog_view.findViewById(R.id.et_data);

                DBAccess dbAccess2 = DBAccess.getInstance(getApplicationContext());
                dbAccess2.open();
                cursor_voto = dbAccess2.getVotoEsameSuperato(insegnamentoSelezionato);
                cursor_voto.moveToFirst();
                cursor_data = dbAccess2.getDataEsameSuperato(insegnamentoSelezionato);
                cursor_data.moveToFirst();
                dbAccess2.close();

                voto.setText(cursor_voto.getString(cursor_voto.getColumnIndex("Voto")));
                data.setText(cursor_data.getString(cursor_data.getColumnIndex("DataSuperamento")));

                data.setInputType(InputType.TYPE_NULL);
                data.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v){
                        Calendar calendar = Calendar.getInstance();
                        datePickerDialog = new DatePickerDialog(FourthRegistrationActivity.this, new OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                Calendar newDate = Calendar.getInstance();
                                newDate.set(year, monthOfYear, dayOfMonth);
                                data.setText(dateFormat.format(newDate.getTime()));

                                //Controllo della data
                                // Mi ricavo la data di oggi
                                final Calendar currentDate = Calendar.getInstance();
                                currentDate.setTime(new Date());
                                // La confronto con quella inserita si poteva usare anche la before
                                if (!newDate.after(currentDate)){
                                    //Toast.makeText(FourthRegistrationActivity.this , "Data Corretta , Esame Registrato" , Toast.LENGTH_LONG).show();
                                    datacorretta = true;
                                }else {
                                    //Toast.makeText(FourthRegistrationActivity.this , "Data Errata perchè del futuro!! \n Reinseriscila corretta !!!" , Toast.LENGTH_LONG).show();
                                    datacorretta = false;
                                }



                            }
                        },calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                        datePickerDialog.show();
                    }
                });

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
                        if (!voto.getText().toString().equals("")) {
                            voto_esame = Integer.parseInt(voto.getText().toString());
                            data_esame = data.getText().toString();
                            if ((voto_esame < 18) || (voto_esame > 31)) {
                                Toast.makeText(getApplicationContext(), "Voto Inserito Non Valido", Toast.LENGTH_LONG).show();
                            } else if (data_esame.equals("")) {
                                Toast.makeText(getApplicationContext(), "Data Inserita Non Valida", Toast.LENGTH_LONG).show();
                            } else {
                                if (datacorretta) {
                                    Toast.makeText(FourthRegistrationActivity.this, "Data Corretta , Esame Registrato", Toast.LENGTH_LONG).show();
                                    DBAccess dbAccess = DBAccess.getInstance(getApplicationContext());
                                    dbAccess.open();
                                    dbAccess.setVotoEsameSuperato(insegnamentoSelezionato, voto_esame);
                                    dbAccess.setDataEsameSuperato(insegnamentoSelezionato, data_esame);
                                    dbAccess.close();
                                } else {
                                    Toast.makeText(FourthRegistrationActivity.this, "Data Errata perchè del futuro!! \n Reinseriscila corretta !!! \n Esame non registrato", Toast.LENGTH_LONG).show();
                                }
                            }
                        }else{Toast.makeText(FourthRegistrationActivity.this , "Voto non Inserito !!" , Toast.LENGTH_LONG).show();}
                    }
                });
                Dialog insertDialog = insert.create();
                insertDialog.show();
            }
        });
    }

    @Override
    public void onBackPressed() {
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

    public void onClick_back4(View view){
        DBAccess dbAccess = DBAccess.getInstance(getApplicationContext());
        dbAccess.open();
        dbAccess.setVotoToZero();
        dbAccess.setDataToZero();
        dbAccess.close();
        startActivity(new Intent(getApplicationContext(), ThirdRegistrationActivity.class));
    }

    public void onClick_continue4(View view){
        startActivity(new Intent(getApplicationContext(),FifthRegistrationActivity.class));
        finish();
    }
}
