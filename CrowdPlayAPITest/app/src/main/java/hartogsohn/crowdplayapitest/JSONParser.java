package hartogsohn.crowdplayapitest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import hartogsohn.crowdplayapitest.Model.Example;
import hartogsohn.crowdplayapitest.Model.Item;
import hartogsohn.crowdplayapitest.Model.Tracks;

/**
 * Created by Jonas R. Hartogsohn on 15-05-2017.
 */

public class JSONParser {

    //example of parsing with Gson - not that the Gson parser uses the model object CityWeather, Clouds, Coord, Main, Sys, Weather and Wind extracted with http://www.jsonschema2pojo.org/
    public static Example parseSearchWithJsonParser(String jsonString){

        Gson gson = new GsonBuilder().create();
        Example example =  gson.fromJson(jsonString, Example.class);
        if(example != null) {
            //weatherInfo.time = new Date();
            return example;
        } else {
            return example; //return "could not parse with gson";
        }
    }
}
