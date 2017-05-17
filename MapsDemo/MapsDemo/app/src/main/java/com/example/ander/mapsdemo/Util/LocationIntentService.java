package com.example.ander.mapsdemo.Util;

import android.Manifest;
import android.app.Activity;
import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;

import com.example.ander.mapsdemo.MapsActivity;

/**
 * Created by ander on 17/05/2017.
 */

public class LocationIntentService extends IntentService{

    LocationManager locationManager;
    static final int MY_PERMISSIONS_REQUEST_FINE_LOCATION=0;
    double Lat;
    double Long;
    Activity startingActivity;


    public LocationIntentService(Activity activity){
        super("LocationIntentService");

        startingActivity = activity;
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        if (locationManager == null) {
            locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        }

        Criteria criteria = new Criteria();
        String provider = locationManager.getBestProvider(criteria, false);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // If not, ask for it
            ActivityCompat.requestPermissions(startingActivity,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},MY_PERMISSIONS_REQUEST_FINE_LOCATION);
            // onRequestPermissionsResult is automatically invoked now
        }
        Location location = locationManager.getLastKnownLocation(provider);

        Lat = location.getLatitude();

        Long = location.getLongitude();

        // Broadcast intent with last known latitude and longtitude
        Intent returnintent = new Intent("CurrentUSerLocation");

        returnintent.putExtra("latitude",Double.toString(Lat));
        returnintent.putExtra("longtitude",Double.toString(Long));



        sendBroadcast(returnintent);


    }
}
