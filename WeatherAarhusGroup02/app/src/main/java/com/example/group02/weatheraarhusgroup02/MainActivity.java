package com.example.group02.weatheraarhusgroup02;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.group02.weatheraarhusgroup02.Model.WeatherInfo;
import com.example.group02.weatheraarhusgroup02.Services.WeatherUpdater;


public class MainActivity extends AppCompatActivity {


    WeatherUpdater weatherService;
    boolean serviceBound = false;
    Button btnUpdate;
    WeatherInfo currentWeatherInfo;
    TextView textviewDescription;
    TextView textviewCity;
    TextView textviewTemp;

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

        startStartedService();

        // At "update" button press
        btnUpdate.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                //get weather info
                currentWeatherInfo = weatherService.GetLatestWeather();
                if (currentWeatherInfo!=null)
                {
                    textviewDescription.setText(currentWeatherInfo.weather.get(0).description);
                    textviewCity.setText(currentWeatherInfo.name);
                    double currenttemp = currentWeatherInfo.main.temp-273.15;
                    textviewTemp.setText(String.format( "%.1f", currenttemp )+(char) 0x00B0+" C");
                }
            };
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Bind to LocalService
        Intent intent = new Intent(this, WeatherUpdater.class);
        //bindService(intent, mConnection, this.BIND_AUTO_CREATE);
        // weatherService.SendRequest();
    }

    @Override
    protected void onStop() {
        super.onStop();
        // Unbind from the service
        /*if (serviceBound) {
            unbindService(mConnection);
            serviceBound = false;
        }*/
    }

    /* Defines callbacks for service binding, passed to bindService() */
    /*private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            WeatherUpdater.LocalBinder binder = (WeatherUpdater.LocalBinder) service;
            weatherService = binder.getService();
            serviceBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            serviceBound = false;
        }
    };*/

    public void startStartedService(){
        Intent intent = new Intent(MainActivity.this, WeatherUpdater.class);
        startService(intent);
    }


}
