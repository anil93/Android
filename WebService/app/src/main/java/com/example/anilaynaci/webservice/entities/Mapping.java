package com.example.anilaynaci.webservice.entities;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by anila on 23.10.2017.
 */

public class Mapping {

    public Wheather Mapper(String s) {

        Wheather wheather = new Wheather();

        try {
            //name
            JSONObject jo = new JSONObject(s);
            wheather.setName(jo.getString("name"));

            //sys
            Sys sys = new Sys();

            JSONObject getSys = jo.getJSONObject("sys");

            String country = (String)getSys.get("country");
            sys.setCountry(country);

            wheather.setSys(sys);

            //main

            Main mainclass = new Main();

            JSONObject getWeather = jo.getJSONObject("main");

            Double temp = (Double)getWeather.get("temp");
            mainclass.setTemp(temp);

            Double temp_min = (Double)getWeather.get("temp_min");
            mainclass.setTemp_min(temp_min);

            Double temp_max = (Double)getWeather.get("temp_max");
            mainclass.setTemp_max(temp_max);

            int humidity = (int)getWeather.get("humidity");
            mainclass.setHumidity(humidity);

            Object pressure = getWeather.get("pressure");
            mainclass.setPressure((Integer) pressure);

            wheather.setMain(mainclass);

            //weather

            Weather weather = new Weather();

            JSONArray getWeatherArray = jo.getJSONArray("weather");
            JSONObject getWeatherArray1 = getWeatherArray.getJSONObject(0);

            int id = (int)getWeatherArray1.get("id");
            weather.setId(id);

            String main = (String)getWeatherArray1.get("main");
            weather.setMain(main);

            String description = (String)getWeatherArray1.get("description");
            weather.setDescription(description);

            String icon = (String)getWeatherArray1.get("icon");
            weather.setIcon(icon);

            wheather.setWeather(weather);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return wheather;
    }
}
