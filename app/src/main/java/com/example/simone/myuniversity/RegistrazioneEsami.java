package com.example.simone.myuniversity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
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
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

//Activity per aggiungere alla carriera un esame superato
public class RegistrazioneEsami extends AppCompatActivity {

    ListView listview;
    public int voto_esame;
    public String data_esame;
    DatePickerDialog datePickerDialog;
    SimpleDateFormat dateFormat;
    Boolean datacorretta;
    TextView listaVuota;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrazione_esami);

        listview = (ListView)findViewById(R.id.lv_regExam);
        DBAccess dbAccess = DBAccess.getInstance(getApplicationContext());
        dbAccess.open();
        List<String> nonSuperati = dbAccess.getDaSeguire();
        dbAccess.close();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, nonSuperati);
        listview.setAdapter(adapter);
        listview.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        listview.setEmptyView(findViewById(R.id.tv_regExam_4));

        listaVuota = (TextView)findViewById(R.id.tv_regExam_4);

        listaVuota.setText("Nessun Esame Registabile");

        dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ITALY);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                final String insegnamentoSelezionato = listview.getItemAtPosition(position).toString();

                View dialog_view = (LayoutInflater.from(RegistrazioneEsami.this)).inflate(R.layout.set_voto_data, null);
                AlertDialog.Builder insert = new AlertDialog.Builder(RegistrazioneEsami.this);
                insert.setView(dialog_view);
                final EditText voto = (EditText) dialog_view.findViewById(R.id.et_voto);
                final EditText data = (EditText) dialog_view.findViewById(R.id.et_data);

                data.setInputType(InputType.TYPE_NULL);
                data.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        Calendar calendar = Calendar.getInstance();
                        datePickerDialog = new DatePickerDialog(RegistrazioneEsami.this, new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                Calendar newDate = Calendar.getInstance();
                                newDate.set(year, monthOfYear, dayOfMonth);
                                data.setText(dateFormat.format(newDate.getTime()));

                                //Controllo della data

                                // Mi ricavo la data di oggi
                                final Calendar currentDate = Calendar.getInstance();
                                currentDate.setTime(new Date());
                                // La confronto con quella inserita
                                if (!newDate.after(currentDate)) {
                                    //Toast.makeText(FourthRegistrationActivity.this , "Data Corretta , Esame Registrato" , Toast.LENGTH_LONG).show();
                                    datacorretta = true;
                                } else {
                                    //Toast.makeText(FourthRegistrationActivity.this , "Data Errata perchè del futuro!! \n Reinseriscila corretta !!!" , Toast.LENGTH_LONG).show();
                                    datacorretta = false;
                                }
                            }
                        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
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
                                        //Toast.makeText(getApplicationContext(), "Voto Inserito Non Valido", Toast.LENGTH_LONG).show();
                                    } else if (data_esame.equals("")) {
                                        //Toast.makeText(getApplicationContext(), "Data Inserita Non Valida", Toast.LENGTH_LONG).show();
                                    } else {
                                        if (datacorretta) {
                                            //Toast.makeText(RegistrazioneEsami.this, "Data Corretta , Esame Registrato", Toast.LENGTH_LONG).show();
                                            DBAccess dbAccess = DBAccess.getInstance(getApplicationContext());
                                            dbAccess.open();
                                            dbAccess.setVotoEsameSuperato(insegnamentoSelezionato, voto_esame);
                                            dbAccess.setDataEsameSuperato(insegnamentoSelezionato, data_esame);
                                            dbAccess.close();
                                        } else {
                                            //Toast.makeText(RegistrazioneEsami.this, "Data Errata perchè del futuro!! \n Reinseriscila corretta !!! \n Esame non registrato", Toast.LENGTH_LONG).show();
                                        }

                                        finish();
                                        startActivity(new Intent(getApplicationContext(), RegistrazioneEsami.class));
                                    }
                                } else {
                                    //Toast.makeText(RegistrazioneEsami.this , "Voto non Inserito !!!" , Toast.LENGTH_LONG).show();
                                }
                            }
                        }

                );
                    Dialog insertDialog = insert.create();
                    insertDialog.show();
                }
            });
    }

    public void onClick_regExam_end(View view){
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
