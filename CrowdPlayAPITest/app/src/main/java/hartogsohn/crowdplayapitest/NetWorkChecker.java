package hartogsohn.crowdplayapitest;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

/**
 * Created by Jonas R. Hartogsohn on 15-05-2017.
 */

public class NetWorkChecker {
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
