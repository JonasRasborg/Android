package com.example.group02.weatheraarhusgroup02.Utilities;


import android.app.Application;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Parcelable;
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

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Anders on 05/05/2017.
 */



public class FireBaseConnector extends Application {

    DatabaseReference mRootRef;
    private String FirebaseRoot = "https://weatherapp-d0836.firebaseio.com/";
    FirebaseDatabase firebaseDatabase;
    DatabaseReference mChildRef;

    LocalBroadcastManager localBroadcastManager;
    Intent intent;


    public  FireBaseConnector()
    {
        mRootRef = FirebaseDatabase.getInstance().getReference();

        localBroadcastManager  = LocalBroadcastManager.getInstance(this);

        intent = new Intent("AllWeathers");


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

    public void putData(WeatherInfo weatherInfo)
    {
        mRootRef.push().setValue(weatherInfo);
    }

}
