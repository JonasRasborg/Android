package com.example.group02.weatheraarhusgroup02.Utilities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

/**
 * Created by Rune Rask on 03-05-2017.
 */

import static com.example.group02.weatheraarhusgroup02.Utilities.Globals.CONNECT;

public class NetworkChecker {

    public static boolean getNetworkStatus(Context c){
        ConnectivityManager connectMan = (ConnectivityManager)c.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = connectMan.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnected()) {
            //somehow we connected
            Log.d(CONNECT, "Got connections" +
                    netInfo.toString()
            );

            //return "Got connections" + netInfo.toString();
            return  true;
        } else {
            //oh no, no connection
            Log.d(CONNECT, "No connections");
            //return "No connections";
            return false;
        }
    }
}
