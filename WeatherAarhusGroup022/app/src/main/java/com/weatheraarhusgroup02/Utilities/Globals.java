package com.weatheraarhusgroup02.Utilities;

/**
 * Created by Rune Rask on 03-05-2017.
 */

public class Globals {

    public static final String CONNECT = "CONNECTIVITY";

    public static final String WEATHER_API_KEY = "e9474d42761603ae3edd024323fdad3e";

    public static final long CITY_ID_AARHUS = 2624652;

    public static final String WEATHER_API_CALL = "http://api.openweathermap.org/data/2.5/weather?id=" + CITY_ID_AARHUS + "&APPID=" + WEATHER_API_KEY;

    public static final double TO_CELCIOUS_FROM_KELVIN = -273.15;

    //public static final String FIREBASE_ROOT = "https://weatherapp-7ad3b.firebaseio.com/";

}
