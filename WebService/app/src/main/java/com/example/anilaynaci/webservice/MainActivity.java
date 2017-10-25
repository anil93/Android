package com.example.anilaynaci.webservice;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.anilaynaci.webservice.entities.Mapping;
import com.example.anilaynaci.webservice.entities.Wheather;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    byte[] icon;

    Drawable drawable;

    String latitude;
    String longitude;

    TextView txtLatLon;
    TextView txtDesc;
    TextView txtMain;
    TextView txtTempMin;
    TextView txtTempMax;
    TextView txtHumidity;
    TextView txtPressure;
    ImageView imgIcon;

    LinearLayout topLayout;
    LinearLayout middleLayout;
    LinearLayout bottomLayout;

    LocationManager lm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    public void init() {

        txtLatLon = (TextView) findViewById(R.id.txtLatLon);
        txtDesc = (TextView) findViewById(R.id.txtDesc);
        txtMain = (TextView) findViewById(R.id.txtMain);
        txtTempMin = (TextView) findViewById(R.id.txtTempMin);
        txtTempMax = (TextView) findViewById(R.id.txtTempMax);
        txtHumidity = (TextView) findViewById(R.id.txtHumidity);
        txtPressure = (TextView) findViewById(R.id.txtPressure);

        imgIcon = (ImageView) findViewById(R.id.imgIcon);

        topLayout = (LinearLayout) findViewById(R.id.TopLayout);
        middleLayout = (LinearLayout) findViewById(R.id.MiddleLayout);
        bottomLayout = (LinearLayout) findViewById(R.id.BottomLayout);

        getGeoCoord();
    }

    //region GPS verisi alma

    public class GpsReceiver implements LocationListener {

            @Override
            public void onLocationChanged(Location location) {

                if (location != null) {
                    latitude = Double.toString(location.getLatitude());
                    longitude = Double.toString(location.getLongitude());

                    new Background().
                            execute(String.format("http://api.openweathermap.org/data/2.5/weather?lat=" + latitude + "&lon=" + longitude + "&APPID=b41774e3395246a8fcbc2eb880961c38"));
                } else {
                    Toast.makeText(MainActivity.this, "Konum bilgisi alınamıyor", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {
                //txtLatLon.setText("GPS açık. Konum verisi alınıyor...");
            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        }

    public boolean isGPSEnabled() {

        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            return false;
        } else {
            return true;
        }
    }

    public void getGeoCoord() {

        if (!isGPSEnabled()) {
            txtLatLon.setText("GPS kapalı! Konum verisi alınamıyor.");
        } else {
            txtLatLon.setText("Konum verisi alınıyor...");
        }

        GpsReceiver receiver = new GpsReceiver();
        lm = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 9999999L, 9999999.0F, receiver);
    }

    //endregion

    //region Web service ten veri çekme ve ekrana yazdırma

    class Background extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {

            Service service = new Service();
            String data = service.getData(params[0]);

            try {
                JSONObject jo = new JSONObject(data);
                JSONArray getWeatherArray = jo.getJSONArray("weather");
                JSONObject getWeatherArray1 = getWeatherArray.getJSONObject(0);

                //service'ten iconu alma

                String iconCode = String.format(getWeatherArray1.get("icon")+".png");
                drawable = service.getDrawable(iconCode);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return data;
        }

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onPostExecute(String s) {

            Wheather wheather;
            Mapping mapping = new Mapping();
            wheather = mapping.Mapper(s);

            try{
                String city = wheather.getName();
                String country = wheather.getSys().getCountry();
                txtLatLon.setText(country+" ,"+city);
                txtDesc.setText(wheather.getWeather().getDescription());
                txtMain.setText(wheather.getWeather().getMain());
                int tempMin = (int)(Math.round(wheather.getMain().getTemp_min())-273d);
                txtTempMin.setText(String.format(Integer.toString(tempMin)+"°C"));
                int tempMax = (int)(Math.round(wheather.getMain().getTemp_max())-273d);
                txtTempMax.setText(String.format(Integer.toString(tempMax)+"°C"));
                txtHumidity.setText(String.format(Integer.toString(wheather.getMain().getHumidity()))+" %");
                txtPressure.setText(String.format(Integer.toString(wheather.getMain().getPressure()))+" hPa");

                imgIcon.setImageDrawable(drawable);

                middleLayout.setVisibility(View.VISIBLE);
                bottomLayout.setVisibility(View.VISIBLE);
            }
            catch(Exception e){
                e.printStackTrace();
            }
        }
    }
    //endregion
}