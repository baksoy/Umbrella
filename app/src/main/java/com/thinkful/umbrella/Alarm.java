package com.thinkful.umbrella;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.Calendar;

public class Alarm {
    public void setAlarm(Context context, String latitude, String longitude) {

        //get reference to AlarmManager
        AlarmManager alarmMgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);

        //Elapse real time repeating (test alarm)
/*
        alarmMgr.setInexactRepeating(AlarmManager.ELAPSED_REALTIME,
                SystemClock.elapsedRealtime() + 10 * 1000,
                10 * 1000, getBroadcastActivityPendingIntent(context,latitude,longitude));
*/

        //RTC alarm repeating
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 7);
        calendar.set(Calendar.MINUTE, 30);
        long milliseconds = calendar.getTimeInMillis();

        alarmMgr.setInexactRepeating(AlarmManager.RTC, milliseconds,
                AlarmManager.INTERVAL_DAY, getBroadcastActivityPendingIntent(context,latitude,longitude));

        Log.i("alarm","Set alarm");
    }
    protected void cancelAlarm(Context context, String latitude, String longitude) {
        AlarmManager alarmMgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        alarmMgr.cancel(getBroadcastActivityPendingIntent(context, latitude, longitude));

        Log.i("alarm","Cancel alarm");
    }
    protected PendingIntent getBroadcastActivityPendingIntent(Context context, String latitude, String longitude) {
        Intent intent = new Intent(context, AlarmBroadcastReceiver.class);
        intent.putExtra(NotifyService.LATITUDE, latitude);
        intent.putExtra(NotifyService.LONGITUDE, longitude);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
        return(pendingIntent);
    }
}
