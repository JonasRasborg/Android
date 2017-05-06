package com.weatheraarhusgroup02.Utilities;


import android.app.Application;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.ValueEventListener;
import com.weatheraarhusgroup02.Model.WeatherInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Anders on 05/05/2017.
 */



public class FireBaseConnector extends Application {

    DatabaseReference mRootRef;
    Boolean saved = null;
    ArrayList<String> weatherList = new ArrayList<>();
    private String FirebaseRoot = "https://weatherapp-7ad3b.firebaseio.com/";
    FirebaseDatabase firebaseDatabase;


    LocalBroadcastManager localBroadcastManager;
    Intent intent;


    public FireBaseConnector(DatabaseReference db) {
        mRootRef = db;
    }

    public ArrayList<String> retrieve(){
        mRootRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                fetchData(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return weatherList;
    }

    public void fetchData(DataSnapshot dataSnapshot)
    {
        weatherList.clear();
        String description;
        double celcious;
        String temp;
        Date time;

        for (DataSnapshot thissnapshot:dataSnapshot.getChildren())
        {

            description = thissnapshot.getValue(WeatherInfo.class).weather.get(0).description;
            celcious = thissnapshot.getValue(WeatherInfo.class).main.temp + Globals.TO_CELCIOUS_FROM_KELVIN;
            temp = Double.toString(celcious);
            time = new Date((int)thissnapshot.getValue(WeatherInfo.class).dt*1000);


            weatherList.add(description + " " + temp + "\n" + time);
        }

    }
    /*

        //localBroadcastManager  = LocalBroadcastManager.getInstance(this);

        //intent = new Intent("AllWeathers");


        mRootRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {

                ArrayList<WeatherInfo> weathers = new ArrayList<WeatherInfo>();

                for (DataSnapshot thissnapshot:dataSnapshot.getChildren())
                {
                    WeatherInfo weatherInfo = new WeatherInfo();
                    weatherInfo = thissnapshot.getValue(WeatherInfo.class);
                    weathers.add(weatherInfo);
                }


                Bundle b = new Bundle();
                b.putSerializable("weathers", weathers);
                intent.putExtra("weathers", b);
                localBroadcastManager.sendBroadcast(intent);


                Log.d("FireBaseConnector", "datasnapshot EXISTS");
            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {
                Log.d("FireBaseConnector", "datasnapshot doesnt exist");
            }
        });

    }
*/
    public void putData(WeatherInfo weatherInfo)
    {
        mRootRef.push().setValue(weatherInfo);
    }

}
