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



public class FireBaseConnector {

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

    public void putData(WeatherInfo weatherInfo)
    {
        mRootRef.push().setValue(weatherInfo);
    }

}
