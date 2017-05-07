package com.weatheraarhusgroup02.Services;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.IntDef;
import android.util.Log;
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

    private static final long TIMERTASK_INTERVAL = 10 * 1000; // Sætter intervallet mellem TimerTask bliver kaldt

    private Handler mHandler = new Handler(); //Opretter en handler til baggrundstråden

    private Timer mTimer = null;

    private NetworkChecker networkChecker;

    private WebConnector webConnector;


    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "onCreate, Thread name: " + Thread.currentThread().getName());

        if(mTimer != null){
            mTimer.cancel();
        }
        else{
            mTimer = new Timer();
        }

        networkChecker = new NetworkChecker();
        webConnector = new WebConnector();


        mTimer.scheduleAtFixedRate(new MyTimerTask(), 0, TIMERTASK_INTERVAL);

        //new MyAsyncTask().execute();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.i(TAG,"onStartedCommand, Thread name: " + Thread.currentThread().getName());

        return START_REDELIVER_INTENT;
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.i(TAG,"onBind, Thread name: " + Thread.currentThread().getName());
        return null;
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


    class MyTimerTask extends TimerTask {
        private final String TAG = MyTimerTask.class.getSimpleName();

        @Override
        public void run() {
            Log.i(TAG,"beforeHandler, Thread name: " + Thread.currentThread().getName());
            //final String theTime = getDateTime();
            GetLatestWeather();


            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(), "Weather downloaded", Toast.LENGTH_SHORT).show();
                    Log.i("RUNNABLE","onRun, Thread name: " + Thread.currentThread().getName());
                }
            });



        }

    }

    class MyAsyncTask extends android.os.AsyncTask<Void,String,Void>{

        private final String TAG = MyAsyncTask.class.getSimpleName();

        @Override
        protected Void doInBackground(Void... params) {

            Log.i(TAG,"doInBackground, Thread name: " + Thread.currentThread().getName());

            int i = 0;

            while(i<=10){
                Log.i(TAG,"doInBackground, Thread name: " + Thread.currentThread().getName());
                publishProgress("The count is now: " + i);

                try {
                    Thread.sleep(4000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                i++;
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);

            Toast.makeText(WeatherUpdateService.this, values[0], Toast.LENGTH_SHORT).show();

            Log.i(TAG, "onProgressUpdate, Thread name: " + Thread.currentThread().getName() + " Counter: " + values[0]);
        }
    }
}
