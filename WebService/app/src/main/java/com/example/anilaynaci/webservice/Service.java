package com.example.anilaynaci.webservice;

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
        try {
            URL url = new URL(serviceURL);
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

    public byte[] getIcon(String serviceIconURL) {
        HttpURLConnection connection = null ;
        InputStream is = null;
        ByteArrayOutputStream baos = null;
        try {
            URL url = new URL(serviceIconURL);
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            is = connection.getInputStream();
            byte[] buffer = new byte[1024];
            baos = new ByteArrayOutputStream();
            while ( is.read(buffer) != -1)
                baos.write(buffer);

            return baos.toByteArray();
        }
        catch(Throwable t) {
            t.printStackTrace();
        }
        finally {
            try { is.close(); } catch(Throwable t) {}
            try { connection.disconnect(); } catch(Throwable t) {}
        }

        return null;
    }
}
