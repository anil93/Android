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

            //main
            JSONObject getWeather = jo.getJSONObject("main");

            Double temp = (Double)getWeather.get("temp");
            wheather.getMain().setTemp(temp);

            Double temp_min = (Double)getWeather.get("temp_min");
            wheather.getMain().setTemp_min(temp_min);

            Double temp_max = (Double)getWeather.get("temp_max");
            wheather.getMain().setTemp_max(temp_max);

            int humidity = (int)getWeather.get("humidity");
            wheather.getMain().setHumidity(humidity);

            int pressure = (int)getWeather.get("pressure");
            wheather.getMain().setPressure(pressure);

            //weather
            JSONArray getWeatherArray = jo.getJSONArray("weather");
            JSONObject getWeatherArray1 = getWeatherArray.getJSONObject(0);

            int id = (int)getWeatherArray1.get("id");
            wheather.getWeather().setId(id);

            String main = (String)getWeatherArray1.get("main");
            wheather.getWeather().setMain(main);

            String description = (String)getWeatherArray1.get("description");
            wheather.getWeather().setDescription(description);

            String icon = (String)getWeatherArray1.get("icon");
            wheather.getWeather().setIcon(icon);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return wheather;
    }
}
