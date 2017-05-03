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
import android.widget.Toast;

import com.example.group02.weatheraarhusgroup02.Model.WeatherInfo;
import com.example.group02.weatheraarhusgroup02.Services.WeatherUpdater;
import com.example.group02.weatheraarhusgroup02.Utilities.Globals;
import com.example.group02.weatheraarhusgroup02.Utilities.NetworkChecker;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpStack;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;


public class MainActivity extends AppCompatActivity {


    WeatherUpdater weatherService;
    boolean serviceBound = false;
    Button btnUpdate;
    WeatherInfo currentWeatherInfo;
    TextView textviewCloudy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Make instances
        btnUpdate = (Button) findViewById(R.id.btnUpdate);
        currentWeatherInfo = new WeatherInfo();
        textviewCloudy = (TextView) findViewById(R.id.textViewCloudy);

        btnUpdate.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                currentWeatherInfo = weatherService.SendRequest();
                if (currentWeatherInfo!=null)
                {
                    textviewCloudy.setText(currentWeatherInfo.wind.speed.toString());
                }
            };
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Bind to LocalService
        Intent intent = new Intent(this, WeatherUpdater.class);
        bindService(intent, mConnection, this.BIND_AUTO_CREATE);
       // weatherService.SendRequest();
    }

    @Override
    protected void onStop() {
        super.onStop();
        // Unbind from the service
        if (serviceBound) {
            unbindService(mConnection);
            serviceBound = false;
        }
    }

    /** Defines callbacks for service binding, passed to bindService() */
    private ServiceConnection mConnection = new ServiceConnection() {

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
    };


}
