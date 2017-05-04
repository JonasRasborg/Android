package com.example.smap.weatherassignment.Services;

/**
 * Created by Anders on 04/05/2017.
 */

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
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

    Intent localIntent;
    Intent weatherIntent;
    int a = 0;
    LocalBroadcastManager lbcm;

    Toast t;

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

                            weatherIntent.putExtra("description", weatherInfo.weather.get(0).description);
                            weatherIntent.putExtra("name", weatherInfo.name);
                            weatherIntent.putExtra("temp", weatherInfo.main.temp - 273.15);

                            a++;
                            localIntent.putExtra("int", a);

                            lbcm.sendBroadcast(localIntent);
                            lbcm.sendBroadcast(weatherIntent);
                        }

                        Log.d("Thread", "Thread is running");
                        wait(5000);

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
        localIntent = new Intent("ACTION");
        weatherIntent = new Intent("weatherUpdate");
        lbcm = LocalBroadcastManager.getInstance(this);
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
