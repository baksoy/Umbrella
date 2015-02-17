package com.thinkful.umbrella;

import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;


public class MainActivity extends ActionBarActivity implements ConnectionCallbacks {
    private GoogleApiClient mGoogleApiClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        WebServiceTask webServiceTask = new WebServiceTask();
        webServiceTask.execute("Bera Aksoy");

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        Location mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        LatLng latLng = new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
        Log.i("BAK", "latLng");
    }


    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class WebServiceTask extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            String useUmbrellaStr = "Don't know, sorry about that.";
            HttpURLConnection urlConnection = null;

            Location mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            LatLng latLng = new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());

            try {
                Double mLatitude = latLng.latitude;
                Double mLongitude = latLng.longitude;

                //URL url = new URL("http://api.openweathermap.org/data/2.5/forecast/daily?q=Washington,D.C.&mode=json&units=metric&cnt=1");

                URL url = new URL("http://api.openweathermap.org/data/2.5/find?lat="+mLatitude+"&lon="+mLongitude+"&cnt=10");
                urlConnection = (HttpURLConnection) url.openConnection();
                useUmbrellaStr = useUmbrella(urlConnection.getInputStream());
            } catch (IOException e) {
                Log.e("MainActivity", "Error ", e);
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }
            return useUmbrellaStr;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            TextView textView = (TextView) findViewById(R.id.hello);
            textView.setText("Should I take and umbrella today? " + s);
        }


        protected String useUmbrella(InputStream in) {
            StringBuilder stringBuilder = new StringBuilder();
            BufferedReader bufferedReader = null;
            try {
                bufferedReader = new BufferedReader(new InputStreamReader(in));
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line + "\n");
                }

                JSONObject forecastJson = new JSONObject(stringBuilder.toString());

                JSONArray weatherArray = forecastJson.getJSONArray("list");
                JSONObject todayForecast = weatherArray.getJSONObject(0);

                // String city = forecastJson.getJSONObject("city").getJSONObject("name").toString(); -- not sure why this is not working

                if (todayForecast.has("rain") || todayForecast.has("snow")) {
                    //return city + "Yes";  -- Not sure why this is not working
                    return "Yes";
                } else {
                    return ("No");
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
            return "Don't know, sorry about that.";
        }

    }
}
