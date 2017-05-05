package com.example.group02.weatheraarhusgroup02.Services;


import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.IntDef;
import android.util.Log;
import android.widget.Toast;

import com.example.group02.weatheraarhusgroup02.Model.WeatherInfo;
import com.example.group02.weatheraarhusgroup02.Utilities.JsonParser;
import com.example.group02.weatheraarhusgroup02.Utilities.NetworkChecker;
import com.example.group02.weatheraarhusgroup02.Utilities.WebConnector;

public class WeatherUpdater extends Service {

    //private final IBinder mBinder = new LocalBinder();

    private static final String TAG = WeatherUpdater.class.getSimpleName();

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
        Log.i(TAG, "onCreate, Thread name " + Thread.currentThread().getName());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.d(TAG, "onStartCommand, Thread name " + Thread.currentThread().getName());

        new WeatherAsyncTask().execute();

        return START_REDELIVER_INTENT;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        //throw new UnsupportedOperationException("Not yet implemented");
        return null;
    }
    /*public class LocalBinder extends Binder {
        public WeatherUpdater getService() {
            return WeatherUpdater.this;
        }
    }*/

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

    class WeatherAsyncTask extends AsyncTask<Void, WeatherInfo, Void>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            int i = 0;
            while(i < 20) {
                WeatherInfo wi = GetLatestWeather();
                Log.i(TAG,"TRYING TO GET WEATHER");
                if (wi != null) {
                    publishProgress(wi);
                    Log.i(TAG,"WeatherInfo= " + wi.name);
                }
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                i++;
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(WeatherInfo... values) {
            super.onProgressUpdate(values);

            Toast.makeText(WeatherUpdater.this, values[0].name,Toast.LENGTH_SHORT).show();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }

}
