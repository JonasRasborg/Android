package com.example.group02.weatheraarhusgroup02.Services;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import com.example.group02.weatheraarhusgroup02.Model.Weather;

public class WeatherUpdater extends Service {

    private final IBinder mBinder = new LocalBinder();

    public WeatherUpdater() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        //throw new UnsupportedOperationException("Not yet implemented");
        return mBinder;
    }
    public class LocalBinder extends Binder {
        WeatherUpdater getService() {
            return WeatherUpdater.this;
        }
    }
}
