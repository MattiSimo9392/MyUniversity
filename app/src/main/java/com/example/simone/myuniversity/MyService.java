package com.example.simone.myuniversity;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.Calendar;


public class MyService extends Service {

    MyBroadcast myBroadcast = new MyBroadcast();
    IntentFilter intentFilter = new IntentFilter();
    PowerManager pm;
    PowerManager.WakeLock wakeLock;

    Cursor esamipernotifica;

    @Override
    public void onCreate(){
        super.onCreate();
        pm = (PowerManager)getSystemService(POWER_SERVICE);
        wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK , "CollectData");
        wakeLock.acquire();

        Log.d("Controllo" , "nel metodo onCreate di Service");
    }
    @Override
    public int onStartCommand(Intent intent , int flagsid , int start){
        super.onStartCommand(intent, flagsid, start);

        intentFilter.addAction(Intent.ACTION_SCREEN_OFF);
        intentFilter.addAction(Intent.ACTION_TIME_TICK);
        intentFilter.addAction(Intent.ACTION_TIME_CHANGED);
        registerReceiver(myBroadcast, intentFilter);
        //registerReceiver(myBroadcast , new IntentFilter(Intent.ACTION_SCREEN_OFF));
        Log.d("Controllo", "nel metodo onStartCommand di Service");
        //Controllo la data
        Calendar now = Calendar.getInstance();
        now.clear(Calendar.SECOND);
        now.clear(Calendar.MILLISECOND);

        // per settare la data decided mi metto nei get prenotati e setto
        DBAccess db = DBAccess.getInstance(getApplicationContext());
        db.open();
        esamipernotifica = db.getExamsDate();
        esamipernotifica.moveToFirst();
        Boolean atleast1examtonotify;       // booleano che passa a true se deve notificare almeno 1 esame
        while (!esamipernotifica.isAfterLast()){
            // Codice | Insegnamento | DataSuperamento | Data_1 | Ora_1 | Data_2 | Ora_2
            //Estraggo gli esami che devo sostenere e confronto le date
            Menu.Data datanotifica = new Menu().new Data();
            // Se la prima data è quella giusta allora mi prendo la prima ora e da li imposto la notifica
            if (esamipernotifica.getString(2).equals(esamipernotifica.getString(3))) {  //confronto Data Superamento con Data_1
                // tiro fuori la data
                datanotifica.DateSplitterV2(esamipernotifica.getString(3));
                datanotifica.DateSplitterV3(esamipernotifica.getString(4));
                atleast1examtonotify = true;
            } else if (esamipernotifica.getString(2).equals(esamipernotifica.getString(5))) {   //confronto Data Superamento con Data_2
                datanotifica.DateSplitterV2(esamipernotifica.getString(5));
                datanotifica.DateSplitterV3(esamipernotifica.getString(6));
                atleast1examtonotify = true;
            }else{atleast1examtonotify = false;}
            // non mi pongo il problema delle 24 ore in quanto non è possibile che un esame sia prenotato alle 00:00
            // se almeno c'è un esame da notificare si manda la notifica
            // controllo messo nel caso in cui non si setti la classe con tutti i parametri a zero settando un notifica per
            // il 0/0/0 alle 00:00
            if(atleast1examtonotify){
                Calendar decided = Calendar.getInstance();
                // month -1 perche gennaio è lo 0
                decided.set(datanotifica.year, datanotifica.month - 1, datanotifica.day, datanotifica.hourofexam - 2, datanotifica.minuteofexam, 0);
                decided.clear(Calendar.SECOND);
                decided.clear(Calendar.MILLISECOND);
                System.out.println("DataNotifica: " + decided.getTime());
                System.out.println("DataCorrente: " + now.getTime());


                if (now.equals(decided)) {
                    Log.d("Controllo", "Mandata la notifica");
                    Notification notifica = new Notification.Builder(this)
                            .setAutoCancel(false)
                            .setContentTitle("Esame Oggi!!!")
                            .setContentText("Oggi hai l'esame di: " + esamipernotifica.getString(1) + "  alle : "+ datanotifica.hourofexam + ":" + datanotifica.minuteofexam + "0" )
                            .setVibrate(new long[]{1000, 1000})
                            .setSmallIcon(R.drawable.exam_icon_v2)
                            .build();

                    NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                    nm.notify(0, notifica);
                }
            }
            esamipernotifica.moveToNext();      //se ci fossero più esami da notificare passa al sucessivo
        }
        return 0;

    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}