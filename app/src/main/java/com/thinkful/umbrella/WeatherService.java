package com.thinkful.umbrella;


import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class WeatherService {
    public static final int CLEAR = 0;
    public static final int RAIN = 1;
    public static final int SNOW = 2;
    public static final int DONTKNOW = 3;

    public WeatherService() {
    }

    public int getWeather(String... params) {
        HttpURLConnection urlConnection = null;
        int useUmbrellaResponse = DONTKNOW;
        try {
            URL url = new URL("http://api.openweathermap.org/data/2.5/forecast/daily?lat="+params[0]+"&lon="+params[1]+"&mode=json&units=metric&cnt=1");
            urlConnection = (HttpURLConnection) url.openConnection();
            useUmbrellaResponse = useUmbrella(urlConnection.getInputStream());
        } catch (IOException e) {
            Log.e("MainActivity", "Error ", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        return useUmbrellaResponse;
    }
    protected int useUmbrella(InputStream in) {
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(in));

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line + "\n");
            }
            Log.i("JSON Response",stringBuilder.toString());
            //Parse JSON
            JSONObject forecastJson = new JSONObject(stringBuilder.toString());
            JSONArray weatherArray = forecastJson.getJSONArray("list");
            JSONObject todayForecast = weatherArray.getJSONObject(0);

            if (todayForecast.has("snow")) {
                return(SNOW);
            } else if (todayForecast.has("rain")) {
                return(RAIN);
            } else {
                return(CLEAR);
            }
        } catch (Exception e) {
            Log.e("MainActivity", "Error", e);
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (final IOException e) {
                    Log.e("PlaceholderFragment", "Error closing stream", e);
                }
            }
        }

        return DONTKNOW;
    }
}
