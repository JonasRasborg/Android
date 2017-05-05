package com.example.group02.weatheraarhusgroup02.Utilities;


import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;

import com.example.group02.weatheraarhusgroup02.Model.Weather;
import com.example.group02.weatheraarhusgroup02.Model.WeatherInfo;
import com.example.group02.weatheraarhusgroup02.Services.WeatherUpdater;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Anders on 05/05/2017.
 */



public class FireBaseConnector {

    DatabaseReference mRootRef;
    private String FirebaseRoot = "https://weatherapp-d0836.firebaseio.com/";
    FirebaseDatabase firebaseDatabase;
    DatabaseReference mChildRef;

    private LocalBroadcastManager localBroadcastManager;
    Intent intent;


    public  FireBaseConnector()
    {
        mRootRef = FirebaseDatabase.getInstance().getReference();

        mChildRef=mRootRef.child("Weathers");

        localBroadcastManager = LocalBroadcastManager.getInstance(this);

        intent = new Intent("WeatherUpdate");


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

                Log.d("FireBaseConnector", "datasnapshot EXISTS");
            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {
                Log.d("FireBaseConnector", "datasnapshot doesnt exist");
            }
        });

    }

    public void putData(WeatherInfo weatherInfo)
    {
        mRootRef.push().setValue(weatherInfo);
    }

}
