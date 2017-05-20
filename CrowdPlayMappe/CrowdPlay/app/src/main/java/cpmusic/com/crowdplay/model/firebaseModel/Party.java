package cpmusic.com.crowdplay.model.firebaseModel;


import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

/**
 * Created by rrask on 18-05-2017.
 */

public class Party {
    public String name;
    public String password;
    public ArrayList<Track> tracks;
    public CustomLatLng clatLong;

    public Party()
    {

    }

    public Party(String _name, String _password, CustomLatLng _latLong){
        name = _name;
        password = _password;
        clatLong = _latLong;
        tracks = new ArrayList<>();
    }


}
