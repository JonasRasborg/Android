package com.example.smap.weatherassignment;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.smap.weatherassignment.DTO.WeatherInfo;
import com.example.smap.weatherassignment.Utils.NetworkChecker;
import com.example.smap.weatherassignment.Utils.WebConnector;
import com.example.smap.weatherassignment.Utils.JsonParser;
import com.example.smap.weatherassignment.Services.MyService;


public class MainActivity extends AppCompatActivity {


    // Attributes
    Button btnUpdate;
    String weatherString;
    WeatherInfo currentWeatherInfo;
    TextView textviewDescription;
    TextView textviewCity;
    TextView textviewTemp;

    // Classes
    NetworkChecker networkChecker;
    WebConnector webConnector;


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

        // Classes
        networkChecker = new NetworkChecker();
        webConnector = new WebConnector();

        // Start service
        StartMyService();


        // At "update" button press
        btnUpdate.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view){
                // Run method that updates current weather in GUI
                UpdateCurrentWeather();
            };
        });
    }

    // Methods

    public void UpdateCurrentWeather()
    {

        if(networkChecker.getNetworkStatus(this) == true)
        {
            weatherString = webConnector.sendRequest(this);
            // parse til Gson fil (DTO)
            if (weatherString!=null)
            {
                currentWeatherInfo = JsonParser.parseCityWeatherJsonWithGson(weatherString);
                textviewDescription.setText(currentWeatherInfo.weather.get(0).description);
                textviewCity.setText(currentWeatherInfo.name);
                double currenttemp = currentWeatherInfo.main.temp - 273.15;
                textviewTemp.setText(String.format("%.1f", currenttemp) + (char) 0x00B0 + " C");
            }
        }

    }

    public void StartMyService()
    {
        Intent intent = new Intent(this, MyService.class);
        startService(intent);

    }

    public void StopMyService()
    {
        Intent intent = new Intent(this, MyService.class);
        stopService(intent);

    }








}
