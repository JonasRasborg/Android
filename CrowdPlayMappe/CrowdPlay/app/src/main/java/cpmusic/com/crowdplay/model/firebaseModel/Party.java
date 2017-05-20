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
    public CustomLatLng clatLong;
    public String djFacebookUID;

    public Party()
    {

    }

    public Party(String _name, String _password, CustomLatLng _latLong, String _djFacebookUID){
        name = _name;
        password = _password;
        clatLong = _latLong;
        Tracks = new HashMap<>();
        djFacebookUID = _djFacebookUID;
    }



}
