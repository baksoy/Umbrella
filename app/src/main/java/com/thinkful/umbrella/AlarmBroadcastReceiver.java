package com.thinkful.umbrella;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

public class AlarmBroadcastReceiver extends WakefulBroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("AlarmBroadcastReceiver","latitude = "+intent.getStringExtra(NotifyService.LATITUDE));
        Intent notifyService = new Intent(context, NotifyService.class);
        notifyService.putExtra(NotifyService.LATITUDE, intent.getStringExtra(NotifyService.LATITUDE));
        notifyService.putExtra(NotifyService.LONGITUDE, intent.getStringExtra(NotifyService.LONGITUDE));
        startWakefulService(context, notifyService);
    }
}