package com.example.group02.weatheraarhusgroup02.Services;


import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import com.example.group02.weatheraarhusgroup02.Model.WeatherInfo;
import com.example.group02.weatheraarhusgroup02.Utilities.JsonParser;
import com.example.group02.weatheraarhusgroup02.Utilities.NetworkChecker;
import com.example.group02.weatheraarhusgroup02.Utilities.WebConnector;

public class WeatherUpdater extends Service {

    private final IBinder mBinder = new LocalBinder();

    private WebConnector webConnector;
    private NetworkChecker networkChecker;

    private String thisResponse;
    private WeatherInfo UIweatherInfo;
    private WeatherInfo HistoricweatherInfo;


    public WeatherUpdater()
    {
    }

    @Override
    public void onCreate() {
        super.onCreate();

        webConnector = new WebConnector();
        UIweatherInfo = new WeatherInfo();
        HistoricweatherInfo = new WeatherInfo();
        networkChecker = new NetworkChecker();
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

    public WeatherInfo GetLatestWeather(){


        if(networkChecker.getNetworkStatus(this) == true)
        {
            thisResponse = webConnector.sendRequest(this);
            // parse til Gson fil (DTO)
            UIweatherInfo = JsonParser.parseCityWeatherJsonWithGson(thisResponse);
            return UIweatherInfo;
        }
        else
        {
            return null;
        }

    }



}
