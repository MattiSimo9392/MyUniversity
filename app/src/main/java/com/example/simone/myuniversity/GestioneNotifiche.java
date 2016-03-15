package com.example.simone.myuniversity;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Simone on 15/03/2016.
 */
public class GestioneNotifiche {

    Calendar dataDaNotificare;
    Calendar dataAttuale;

    private Notification getNotification (String content, String title, Context context) {
        Notification.Builder builder  = new Notification.Builder(context);
        builder.setContentTitle(title);
        builder.setContentText(content);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setVibrate(new long[] { 100, 250, 100, 500});
        return builder.build();
        }
    private void scheduleNotification (Notification notification, long delay, Context context) {
        Intent notificationIntent = new Intent(context, NotificationActivity.class);
        notificationIntent.putExtra(NotificationActivity.NOTIFICATION_ID, 1);
        notificationIntent.putExtra(NotificationActivity.NOTIFICATION, notification);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        long futureInMillis = SystemClock.elapsedRealtime() + delay;
        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.ELAPSED_REALTIME, futureInMillis, pendingIntent);
    }

    private long getDelay (Date date){
        dataDaNotificare = Calendar.getInstance();
        dataDaNotificare.setTime(date);
        dataAttuale = Calendar.getInstance();
        long delay = dataDaNotificare.getTimeInMillis() - dataAttuale.getTimeInMillis();
        return delay;
    }
}
