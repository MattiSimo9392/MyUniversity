package com.example.simone.myuniversity;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by Tia on 29/01/2016.
 */

//Adapter per la lista visualizzata nella carriera dell'utente

public class CarrieraCursorAdapter extends CursorAdapter{

    public CarrieraCursorAdapter(Context context , Cursor cursor ){
        super(context , cursor , 0);
    }

    @Override
    public View newView(Context context , Cursor cursor , ViewGroup parent){
        return LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_2, null);
    }

    @Override
    public void bindView(View view , Context context , Cursor cursor ){
        ((TextView) view.findViewById(android.R.id.text1)).setTextColor(0xFF000000);
        ((TextView) view.findViewById(android.R.id.text2)).setTextColor(0xFF000000);
        ((TextView) view.findViewById(android.R.id.text1)).setText(cursor.getString(cursor.getColumnIndex("_id")));      // il custom adapter richiede una colonna _id in questo caso ho cambiato il nome a insegnamento
        ((TextView) view.findViewById(android.R.id.text2)).setText("Voto: " + cursor.getString(cursor.getColumnIndex("Voto")) + "\t \t Data Superamento: " + cursor.getString(cursor.getColumnIndex("DataSuperamento")));

    }
}
