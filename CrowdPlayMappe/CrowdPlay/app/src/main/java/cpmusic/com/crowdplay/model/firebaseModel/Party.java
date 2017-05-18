package cpmusic.com.crowdplay.model.firebaseModel;

/**
 * Created by rrask on 18-05-2017.
 */

public class Party {
    public String name;
    public String password;
    public Tracks tracks;
    public Location location;

    public Party(String _name, String _password){
        name = _name;
        password = _password;
        location = new Location();
        tracks = new Tracks();
    }
}
