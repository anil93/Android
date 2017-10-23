package com.example.anilaynaci.webservice.entities;

/**
 * Created by anila on 23.10.2017.
 */

public class Wheather {

    Coordinates coord = new Coordinates();
    Sys sys = new Sys();
    Weather weather = new Weather();
    Main main = new Main();
    String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Coordinates getCoord() {
        return coord;
    }

    public void setCoord(Coordinates coord) {
        this.coord = coord;
    }





    public Sys getSys() {
        return sys;
    }

    public void setSys(Sys sys) {
        this.sys = sys;
    }







    public Weather getWeather() {
        return weather;
    }

    public void setWeather(Weather weather) {
        this.weather = weather;
    }





    public Main getMain() {
        return main;
    }

    public void setMain(Main main) {
        this.main = main;
    }
}

