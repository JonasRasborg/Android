package com.example.group02.weatheraarhusgroup02.Services;


import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;

import com.example.group02.weatheraarhusgroup02.Model.WeatherInfo;
import com.example.group02.weatheraarhusgroup02.R;
import com.example.group02.weatheraarhusgroup02.Utilities.JsonParser;
import com.example.group02.weatheraarhusgroup02.Utilities.WebConnector;

import com.example.group02.weatheraarhusgroup02.Model.Weather;

public class WeatherUpdater extends Service {

    private final IBinder mBinder = new LocalBinder();

    private WebConnector webConnector;
    //private JsonParser jsonParser;
    private String thisResponse;
    private WeatherInfo weatherInfo;

    public WeatherUpdater() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        webConnector = new WebConnector();
        //jsonParser = new JsonParser();
        weatherInfo = new WeatherInfo();

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        //throw new UnsupportedOperationException("Not yet implemented");
        return mBinder;
    }
    public class LocalBinder extends Binder {
        public WeatherUpdater getService() {
            return WeatherUpdater.this;
        }
    }

    public WeatherInfo SendRequest(){

            thisResponse = webConnector.sendRequest(this);
            weatherInfo = JsonParser.parseCityWeatherJsonWithGson(thisResponse);
            return weatherInfo;

    }

}
