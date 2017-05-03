package com.example.group02.weatheraarhusgroup02.Services;


import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;

import com.example.group02.weatheraarhusgroup02.R;
import com.example.group02.weatheraarhusgroup02.Utilities.WebConnector;

import com.example.group02.weatheraarhusgroup02.Model.Weather;

public class WeatherUpdater extends Service {

    private final IBinder mBinder = new LocalBinder();

    private WebConnector connector;

    public WeatherUpdater() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        connector = new WebConnector();
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

    public void SendRequest(){
        connector.sendRequest(this);
    }

}
