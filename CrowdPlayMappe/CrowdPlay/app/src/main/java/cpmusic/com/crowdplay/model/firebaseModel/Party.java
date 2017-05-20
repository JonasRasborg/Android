package cpmusic.com.crowdplay.model.firebaseModel;


import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by rrask on 18-05-2017.
 */

public class Party {
    public String name;
    public String password;
    public HashMap<String,Track> Tracks;
    public HashMap<String,Guest> Guests;
    public CustomLatLng location;
    public String userID;

    public Party()
    {

    }

    public Party(String _name, String _password, CustomLatLng _latLong, String _userID){
        name = _name;
        password = _password;
        location = _latLong;
        Tracks = new HashMap<>();
        userID = _userID;
        Guests = new HashMap<>();
    }



}
