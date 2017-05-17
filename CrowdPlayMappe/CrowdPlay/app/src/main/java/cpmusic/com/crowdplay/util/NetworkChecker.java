package cpmusic.com.crowdplay.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by rrask on 17-05-2017.
 */

public class NetworkChecker {

    public static boolean getNetworkStatus(Context c){
        ConnectivityManager connectMan = (ConnectivityManager)c.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = connectMan.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnected()) {
            //somehow we connected
            //return "Got connections" + netInfo.toString();
            return  true;
        } else {
            //oh no, no connection
            //return "No connections";
            return false;
        }
    }
}
