package com.example.anilaynaci.webservice;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    String latitude;
    String longitude;
    TextView txtWheather;
    TextView txtLatLon;
    TextView txtCity;
    TextView txtDesc;
    TextView txtTemp;
    TextView txtMinTemp;
    TextView txtMaxTemp;
    LocationManager lm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    public void init() {
        txtWheather = (TextView) findViewById(R.id.txtWheather);
        txtLatLon = (TextView) findViewById(R.id.txtLatLon);
        txtCity = (TextView) findViewById(R.id.txtCity);
        txtDesc = (TextView) findViewById(R.id.txtDesc);
        txtTemp = (TextView) findViewById(R.id.txtTemp);
        txtMinTemp = (TextView) findViewById(R.id.txtMinTemp);
        txtMaxTemp = (TextView) findViewById(R.id.txtMaxTemp);
        getGeoCoord();
    }

    public class GpsReceiver implements LocationListener {
        @Override
        public void onLocationChanged(Location location) {
            if (location != null) {
                latitude = Double.toString(location.getLatitude());
                longitude = Double.toString(location.getLongitude());
                txtLatLon.setText("latitude: " + latitude + " - longitude: " + longitude);
                new Background().execute(String.format("http://api.openweathermap.org/data/2.5/weather?lat=" + latitude + "&lon=" + longitude + "&APPID=b41774e3395246a8fcbc2eb880961c38"));
            } else {
                Toast.makeText(MainActivity.this, "Konum bilgisi alınamıyor", Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {
            //Toast.makeText(MainActivity.this, "GPS kapalı!", Toast.LENGTH_LONG).show();
        }
    }

    public boolean GPSEnabled() {
        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            return false;
        } else {
            return true;
        }
    }

    public void getGeoCoord() {
        if (!GPSEnabled()) {
            txtLatLon.setText("GPS KAPALI! Konum verisi alınamıyor.");
        } else {
            txtLatLon.setText("Konum verisi alınıyor...");
        }
        GpsReceiver receiver = new GpsReceiver();
        lm = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 9999999L, 9999999.0F, receiver);
    }

    class Background extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            HttpURLConnection connection = null;
            BufferedReader br = null;
            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream is = connection.getInputStream();
                InputStreamReader isr = new InputStreamReader(is);
                br = new BufferedReader(isr);
                String line;
                String file = "";
                while ((line = br.readLine()) != null) {
                    file += line;
                }
                return file;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "error";
        }

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onPostExecute(String s) {
            try {
                JSONObject jo = new JSONObject(s);
                txtCity.setText(jo.getString("name"));

            } catch (Exception e) {
                e.printStackTrace();
            }


            txtWheather.setText(s);
        }
    }
}