package com.example.group02.weatheraarhusgroup02;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.group02.weatheraarhusgroup02.Model.WeatherInfo;
import com.example.group02.weatheraarhusgroup02.Services.WeatherUpdater;


public class MainActivity extends AppCompatActivity {


    WeatherUpdater weatherUpdater;
    boolean serviceBound = false;
    Button btnUpdate;
    WeatherInfo currentWeatherInfo;
    TextView textviewDescription;
    TextView textviewCity;
    TextView textviewTemp;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // bind controls in Activity
        btnUpdate = (Button) findViewById(R.id.btnUpdate);
        currentWeatherInfo = new WeatherInfo();
        textviewDescription = (TextView) findViewById(R.id.textViewDescription);
        textviewCity = (TextView) findViewById(R.id.textViewCity);
        textviewTemp = (TextView) findViewById(R.id.textViewTemperature);
        LocalBroadcastManager.getInstance(this).registerReceiver(mReceiveLatest,new IntentFilter("LatestWeather"));
        LocalBroadcastManager.getInstance(this).registerReceiver(mReceiveAll,new IntentFilter("AllWeathers"));
        intent = new Intent(this, WeatherUpdater.class);
        startStartedService();



        // At "update" button press
        btnUpdate.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                //get weather info
                currentWeatherInfo = weatherUpdater.GetLatestWeather();
                RenderGUI(currentWeatherInfo);
                Log.d("Main","Button Pushed");
            };
        });
    }


    private BroadcastReceiver mReceiveLatest = new BroadcastReceiver()
    {
        @Override
        public void onReceive(Context context, Intent intent) {

            RenderGUI((WeatherInfo) intent.getExtras().getSerializable("weatherinfo"));
        }
    };

    private BroadcastReceiver mReceiveAll = new BroadcastReceiver()
    {
        @Override
        public void onReceive(Context context, Intent intent) {

            Log.d("Main", "List of ALL Weathers Received (Y)");
        }
    };

    private void RenderGUI(WeatherInfo weatherInfo)
    {
        if (currentWeatherInfo!=null)
        {
            textviewDescription.setText(weatherInfo.weather.get(0).description);
            textviewCity.setText(weatherInfo.name);
            double currenttemp = weatherInfo.main.temp-273.15;
            textviewTemp.setText(String.format( "%.1f", currenttemp )+(char) 0x00B0+" C");
        }
    }

    // When MainActivity starts it Binds to (Bound)Service
    @Override
    protected void onStart() {
        super.onStart();
        bindService(intent, mConnection, this.BIND_AUTO_CREATE); // Binding. After this onServiceConnected is run

    }

    @Override
    protected void onStop() {
        super.onStop();
        // Unbind from the service
        if (serviceBound==true) {
            unbindService(mConnection);
            Log.d("Main","Service UnBound");
            serviceBound = false;
        }
    }

    /* Defines callbacks for service binding, passed to bindService() */
    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            WeatherUpdater.LocalBinder binder = (WeatherUpdater.LocalBinder) service;
            weatherUpdater = binder.getService(); // Returns interface of weatherupdater
            serviceBound = true;
            Log.d("Main","Service Bound");
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            serviceBound = false;
            Log.d("Main","Service Disconnected");

        }
    };

    public void startStartedService(){
        startService(intent);
        Log.d("Main","Service Started");

    }




}
