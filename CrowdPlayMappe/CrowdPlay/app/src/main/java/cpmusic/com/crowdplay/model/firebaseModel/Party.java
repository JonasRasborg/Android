package cpmusic.com.crowdplay.model.firebaseModel;


import java.util.HashMap;

/**
 * Created by rrask on 18-05-2017.
 */

public class Party {
    public String name;
    public String password;
    public HashMap<String,Track> Tracks;
    public HashMap<String,Guest> Guests;
    public Location location;
    public String userID;
    public String partyID;

    public Party()
    {

    }

    public Party(String _name, String _password, Location _location, String _userID, String _partyID){
        name = _name;
        password = _password;
        location = _location;
        Tracks = new HashMap<>();
        userID = _userID;
        Guests = new HashMap<>();
        partyID = _partyID;
    }



}
