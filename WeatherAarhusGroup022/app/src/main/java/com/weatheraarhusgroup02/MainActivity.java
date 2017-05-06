package com.weatheraarhusgroup02;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;


import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.weatheraarhusgroup02.Model.WeatherInfo;
import com.weatheraarhusgroup02.Services.WeatherUpdateService;
import com.weatheraarhusgroup02.Utilities.FireBaseConnector;
import com.weatheraarhusgroup02.Utilities.Globals;

import java.util.Date;

public class MainActivity extends AppCompatActivity {

    ListView weatherListView;
    ArrayAdapter<String> adapter;
    DatabaseReference db;
    FireBaseConnector fireBaseConnector;
    FirebaseListAdapter<WeatherInfo> firebaseListAdapter;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        startWeatherUpdateSerivce();

        weatherListView = (ListView) findViewById(R.id.ltv_weatherList);

        //SETUP FIREBASE
        db = FirebaseDatabase.getInstance().getReference();
        fireBaseConnector = new FireBaseConnector(db);

        //SETUP ADAPTER
        firebaseListAdapter = new FirebaseListAdapter<WeatherInfo>(this,WeatherInfo.class, android.R.layout.simple_list_item_1,db) {
            @Override
            protected void populateView(View v, WeatherInfo model, int position) {

                TextView textView1 = (TextView) v.findViewById(android.R.id.text1);
                TextView textView2 = (TextView) v.findViewById(android.R.id.text2);

                Double temp = (double)Math.round(model.main.temp + Globals.TO_CELCIOUS_FROM_KELVIN);
                Date time = new Date();
                time.setTime((long)model.dt*1000);



                textView1.setText(model.weather.get(0).description + "           " + Double.toString(temp) + "\n" + time);
                //textView2.setText(model.name);

            }
        };

        weatherListView.setAdapter(firebaseListAdapter);
        //adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,fireBaseConnector.retrieve());
        //weatherListView.setAdapter(adapter);



    }


    public void startWeatherUpdateSerivce(){
        Intent intent = new Intent(MainActivity.this, WeatherUpdateService.class);

        startService(intent);
    }
}
