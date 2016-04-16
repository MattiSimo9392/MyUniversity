package com.example.simone.myuniversity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;


public class MyBroadcast extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String check = intent.getAction();
        if(check.equals(Intent.ACTION_TIME_CHANGED) || check.equals(Intent.ACTION_TIME_TICK)) {
            Intent startserviceintent = new Intent(context, MyService.class);
            context.startService(startserviceintent);
        }
    }
}
