package com.example.anilaynaci.webservice;

import android.graphics.drawable.Drawable;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by anila on 24.10.2017.
 */

public class Service {

    public String getData(String serviceURL){
        HttpURLConnection connection = null;
        BufferedReader br = null;
        InputStream is = null;
        try {
            URL url = new URL(serviceURL);
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            is = connection.getInputStream();
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
        finally {
            try { is.close(); } catch(Throwable t) {}
            try { connection.disconnect(); } catch(Throwable t) {}
        }
        return "error";
    }

    public Drawable getDrawable(String iconCode) {
        String IMG_URL = "http://openweathermap.org/img/w/";

        try {
            InputStream is = (InputStream) new URL(IMG_URL+iconCode).getContent();
            Drawable d = Drawable.createFromStream(is, "src name");
            return d;
        } catch (Exception e) {
            return null;
        }
    }
}
