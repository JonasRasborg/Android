package cpmusic.com.crowdplay.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by ander on 20/05/2017.
 */

public class SharedPreferencesData
{
    static final String FACEBOOK_UID = "logged_in_uid";
    static final String FACEBOOK_FIRST_NAME = "logged_in_firstname";
    static final String FACEBOOK_LOGGED_IN_STATUS = "logged in status";


    public static SharedPreferences getSharedPreferences(Context ctx) {
        return PreferenceManager.getDefaultSharedPreferences(ctx);
    }

    public static void setFacebookUID(Context ctx, String facebook_UID) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(FACEBOOK_UID,facebook_UID);
        editor.commit();
    }

    public static  String getFacebookUID(Context ctx)
    {
        return getSharedPreferences(ctx).getString(FACEBOOK_UID,"");
    }

    public static void setLoggidInStatus(Context ctx, boolean status)
    {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putBoolean(FACEBOOK_LOGGED_IN_STATUS,status);
        editor.commit();
    }

    public boolean getLoggedInStatus(Context ctx)
    {
        return  getSharedPreferences(ctx).getBoolean(FACEBOOK_LOGGED_IN_STATUS,false);
    }

    public static void setFacebookFirstName(Context ctx, String facebook_firstName) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(FACEBOOK_FIRST_NAME,facebook_firstName);
        editor.commit();
    }

    public static String getFacebookFirstName(Context ctx)
    {
        return getSharedPreferences(ctx).getString(FACEBOOK_FIRST_NAME,"");
    }

}