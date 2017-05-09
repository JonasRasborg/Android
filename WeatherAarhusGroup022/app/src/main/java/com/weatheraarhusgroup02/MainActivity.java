package com.weatheraarhusgroup02;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.PersistableBundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.weatheraarhusgroup02.Model.WeatherInfo;
import com.weatheraarhusgroup02.Services.WeatherUpdateService;
import com.weatheraarhusgroup02.Utilities.FireBaseConnector;
import com.weatheraarhusgroup02.Utilities.Globals;
import com.weatheraarhusgroup02.Utilities.WebConnector;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    ListView weatherListView;
    ArrayAdapter<String> adapter;
    DatabaseReference db;
    FireBaseConnector fireBaseConnector;
    FirebaseListAdapter<WeatherInfo> firebaseListAdapter;
    FloatingActionButton fabupdate;
    Intent serviceIntent;
    WebConnector webConnector;
    boolean serviceBound = false;
    WeatherUpdateService weatherUpdateService;
    WeatherInfo latestWeather;
    TextView textViewDesc;
    TextView textViewTemp;
    TextView textViewCity;

    String savedCityName,savedDescription,savedTemperature;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i("Main", "onCreate");


        textViewDesc = (TextView)findViewById(R.id.textViewDescription);
        textViewTemp = (TextView)findViewById(R.id.textViewTemp);
        textViewCity = (TextView)findViewById(R.id.textViewCity);

        LocalBroadcastManager.getInstance(this).registerReceiver(mReceiveFromService, new IntentFilter("latestFromService"));

        serviceIntent = new Intent(MainActivity.this, WeatherUpdateService.class);

        webConnector = new WebConnector();
        fabupdate = (FloatingActionButton)findViewById(R.id.fabUpdate);
        fabupdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                weatherUpdateService.getLatestWeatherFromAPI();
                Toast.makeText(getApplicationContext(), "Weather Updated", Toast.LENGTH_SHORT).show();

            }
        });

        weatherListView = (ListView) findViewById(R.id.ltv_weatherList);

        //SETUP FIREBASE
        db = FirebaseDatabase.getInstance().getReference();
        fireBaseConnector = new FireBaseConnector(db);

        //SETUP ADAPTER
        firebaseListAdapter = new FirebaseListAdapter<WeatherInfo>(this,WeatherInfo.class, android.R.layout.simple_list_item_1,db) {
            @Override
            protected void populateView(View v, WeatherInfo model, int position) {

                TextView textView1 = (TextView) v.findViewById(android.R.id.text1);

                Double temp = (double)Math.round(model.main.temp + Globals.TO_CELCIOUS_FROM_KELVIN);
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy - HH:mm");


                textView1.setText("\n" + model.weather.get(0).description + "\n"
                        + Double.toString(temp) + "\u2103 \n" + sdf.format(model.time) + "\n");

            }

//            @Override
//            public int getCount()
//            {
//                if (super.getCount()>5)
//                {
//                    return 5;
//                }
//                else
//                {
//                    return super.getCount();
//                }
//            }

            @Override
            public WeatherInfo getItem(int position) {
                return super.getItem(getCount() - 1 - position);
            }


        };

        weatherListView.setAdapter(firebaseListAdapter);
        //adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,fireBaseConnector.retrieve());
        //weatherListView.setAdapter(adapter);

        if(savedInstanceState != null){
            savedDescription = savedInstanceState.getString("DESC");
            savedCityName = savedInstanceState.getString("NAME");
            savedTemperature = savedInstanceState.getString("TEMP");

            setCurrentWeather();
        }
    }

    private BroadcastReceiver mReceiveFromService = new BroadcastReceiver()
    {
        @Override
        public void onReceive(Context context, Intent intent) {
            latestWeather = (WeatherInfo) intent.getExtras().getSerializable("latestWeather");
            savedDescription = latestWeather.weather.get(0).description;

            Double temp = (double)Math.round(latestWeather.main.temp + Globals.TO_CELCIOUS_FROM_KELVIN);
            savedTemperature = String.valueOf(temp) + "℃";
            savedCityName = latestWeather.name;

            setCurrentWeather();

        }
    };

    @Override
    protected void onStop() {
        super.onStop();
        if(serviceBound)
        {
            unbindService(mConnection);
            Log.i("Main","Service unbound");
            serviceBound = false;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("Main", "onDestroy");


    }

    @Override
    protected void onStart() {
        super.onStart();

        bindService(serviceIntent,mConnection,this.BIND_AUTO_CREATE);
        startWeatherUpdateSerivce();

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString("TEMP",savedTemperature);
        outState.putString("NAME",savedCityName);
        outState.putString("DESC",savedDescription);
        super.onSaveInstanceState(outState);
    }

    public void startWeatherUpdateSerivce(){

        startService(serviceIntent);

    }

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            WeatherUpdateService.LocalBinder binder = (WeatherUpdateService.LocalBinder) service;
            weatherUpdateService = binder.getService();
            serviceBound = true;
            Log.i("Main", "Service bound");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            serviceBound = false;
            Log.i("Main","Service Disconnected");
        }
    };

    public void setCurrentWeather(){
        textViewTemp.setText(savedTemperature);
        textViewCity.setText(savedCityName);
        textViewDesc.setText(savedDescription);
    }
}
