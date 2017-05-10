package com.weatheraarhusgroup02.Services;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.IntDef;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.weatheraarhusgroup02.Model.WeatherInfo;
import com.weatheraarhusgroup02.Utilities.FireBaseConnector;
import com.weatheraarhusgroup02.Utilities.JsonParser;
import com.weatheraarhusgroup02.Utilities.NetworkChecker;
import com.weatheraarhusgroup02.Utilities.WebConnector;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class WeatherUpdateService extends Service {

    public WeatherUpdateService() {
    }

    private static final String TAG = WeatherUpdateService.class.getSimpleName();

    private final IBinder mBinder = new LocalBinder();

    private NetworkChecker networkChecker;

    private WebConnector webConnector;

    private Intent latestWeather;

    private LocalBroadcastManager localBroadcastManager;


    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "onCreate, Thread name: " + Thread.currentThread().getName());

        networkChecker = new NetworkChecker();
        webConnector = new WebConnector();

        latestWeather = new Intent("latestFromService");
        localBroadcastManager = LocalBroadcastManager.getInstance(this);
        LocalBroadcastManager.getInstance(this).registerReceiver(mReceiveFromWeb, new IntentFilter("latestFromWeb"));

        Thread thread = new Thread(new MyThreadClass(1,this));
        thread.start();

    }


    private BroadcastReceiver mReceiveFromWeb = new BroadcastReceiver()
    {
        @Override
        public void onReceive(Context context, Intent intent) {
            latestWeather.putExtra("latestWeather", (WeatherInfo)intent.getExtras().getSerializable("latestWeather"));
            localBroadcastManager.sendBroadcast(latestWeather);
        }
    };


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.i(TAG,"onStartedCommand, Thread name: " + Thread.currentThread().getName());

        return START_STICKY;
    }


    @Override
    public IBinder onBind(Intent intent) {
        Log.i(TAG,"onBind, Thread name: " + Thread.currentThread().getName());
        getLatestWeatherFromAPI();

        return mBinder;
    }

    public class LocalBinder extends Binder {
        public WeatherUpdateService getService(){
            return WeatherUpdateService.this;
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy, Thread name: " + Thread.currentThread().getName());
    }

    private String getDateTime(){
        Log.i(TAG,"getDateTime, Thread name: " + Thread.currentThread().getName());
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss");
        return sdf.format(new Date());
    }

    private void GetLatestWeather(){
        if(networkChecker.getNetworkStatus(this))
        {
            webConnector.sendRequest(this);
        }
    }

    public void getLatestWeatherFromAPI()
    {
        if(networkChecker.getNetworkStatus(this))
        {
            webConnector.sendRequestForLatest(this);
        }
    }




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

                        Date date;
                        date = new Date();
                        if(date.getMinutes() == 00 && date.getSeconds()<6|| date.getMinutes() == 30 && date.getSeconds()<6)
                        {
                            GetLatestWeather();
                            wait(1000*28*60);
                        }
                    }
                    catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
