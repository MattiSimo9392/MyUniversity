package com.example.simone.myuniversity;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

/**
 * Created by Tia on 29/01/2016.
 */
public class CarrieraCursorAdapter extends CursorAdapter{
    public CarrieraCursorAdapter(Context context , Cursor cursor){
        super (context , cursor);
    }

    @Override
    public View newView(Context context , Cursor cursor , ViewGroup parent){
        return LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_2, null);
    }

    @Override
    public void bindView(View view , Context context , Cursor cursor ){
        ((TextView)view.findViewById(android.R.id.text1)).setText(cursor.getString(cursor.getColumnIndex("Insegnamento")));
        ((TextView)view.findViewById(android.R.id.text2)).setText(cursor.getString(cursor.getColumnIndex("Voto")));
    }
}
