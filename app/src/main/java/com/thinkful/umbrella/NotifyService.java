package com.thinkful.umbrella;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;


public class NotifyService extends IntentService {

    public static final String LATITUDE = "LATITUDE";
    public static final String LONGITUDE = "LONGITUDE";

    public NotifyService() {
        super("NotifyService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.i("NotifyService", "latitude = " + intent.getStringExtra(NotifyService.LATITUDE));
        WeatherService weatherService = new WeatherService();
        int weatherResponse = weatherService.getWeather(intent.getStringExtra(LATITUDE),intent.getStringExtra(LONGITUDE));
        if (weatherResponse==WeatherService.RAIN || weatherResponse==WeatherService.SNOW) {
            String weatherType = weatherResponse==WeatherService.SNOW ? "snow" : "rain";
            Notification notification = new Notification();
            notification.createNotification(this,weatherType);
        }
        AlarmBroadcastReceiver.completeWakefulIntent(intent);
    }

}