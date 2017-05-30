package cpmusic.com.crowdplay.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import cpmusic.com.crowdplay.model.spotifyModel.Auth;
import cpmusic.com.crowdplay.model.spotifyModel.Example;

/**
 * Created by Jonas R. Hartogsohn on 17-05-2017.
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

    public static Auth parseAuthWithJsonParser(String jsonString){

        Gson gson = new GsonBuilder().create();
        Auth auth =  gson.fromJson(jsonString, Auth.class);
        if(auth != null) {
            //weatherInfo.time = new Date();
            return auth;
        } else {
            return auth; //return "could not parse with gson";
        }
    }
}
