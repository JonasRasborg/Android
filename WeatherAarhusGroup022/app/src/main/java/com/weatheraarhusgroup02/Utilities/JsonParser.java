package com.weatheraarhusgroup02.Utilities;

import com.weatheraarhusgroup02.Model.WeatherInfo;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Rune Rask on 03-05-2017.
 */

public class JsonParser {



    //example of parsing with Gson - not that the Gson parser uses the model object CityWeather, Clouds, Coord, Main, Sys, Weather and Wind extracted with http://www.jsonschema2pojo.org/
    public static WeatherInfo parseCityWeatherJsonWithGson(String jsonString){

        Gson gson = new GsonBuilder().create();
        WeatherInfo weatherInfo =  gson.fromJson(jsonString, WeatherInfo.class);
        if(weatherInfo != null) {
            //weatherInfo.time = new Date();
            return weatherInfo;
        } else {
            return weatherInfo; //return "could not parse with gson";
        }
    }
}
