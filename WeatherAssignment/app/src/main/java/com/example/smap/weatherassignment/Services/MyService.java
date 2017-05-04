package com.example.smap.weatherassignment.Services;

/**
 * Created by Anders on 04/05/2017.
 */

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.example.smap.weatherassignment.DTO.WeatherInfo;
import com.example.smap.weatherassignment.Utils.JsonParser;
import com.example.smap.weatherassignment.Utils.NetworkChecker;
import com.example.smap.weatherassignment.Utils.WebConnector;


public class MyService extends Service
{
    // Attributes
    WebConnector webConnector;
    String weatherString;
    WeatherInfo currentWeatherInfo;

    // Thread
    final class MyThreadClass implements Runnable
    {
        int service_id;
        Service starterservice;
        WebConnector ThreadWebConnector;
        NetworkChecker networkChecker;
        WeatherInfo weatherInfo;

        MyThreadClass(int service_id, Service service)
        {
            this.service_id = service_id;
            starterservice = service;
            ThreadWebConnector = new WebConnector();
            networkChecker = new NetworkChecker();
            weatherInfo = new WeatherInfo();
        }

        @Override
        public void run()
        {
            synchronized (this)
            {
                while (true) {
                    try {
                        weatherString = ThreadWebConnector.sendRequest(starterservice);
                        if (weatherString!=null)
                        {
                            Log.d("Thread", "Weatherstring was not null");
                            weatherInfo = JsonParser.parseCityWeatherJsonWithGson(weatherString);
                        }
                        Log.d("Thread", "Thread is running");
                        wait(1000);


                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                //stopSelf(service_id);
            }
        }
    }
    // Lifecycle Methods

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        Toast.makeText(this,"MyService Started ...",Toast.LENGTH_LONG).show();
        Thread thread = new Thread(new MyThreadClass(startId,this));
        thread.start();
        return START_STICKY;
    }

    @Override
    public void onCreate()
    {
        super.onCreate();
    }

    @Override
    public void onDestroy()
    {
        Toast.makeText(this,"MyService Destroyed ...",Toast.LENGTH_LONG).show();
    }


    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }
}
