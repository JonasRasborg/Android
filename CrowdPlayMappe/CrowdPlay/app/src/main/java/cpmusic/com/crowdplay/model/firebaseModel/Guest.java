package cpmusic.com.crowdplay.model.firebaseModel;

import com.facebook.FacebookSdk;

/**
 * Created by ander on 19/05/2017.
 */

public class Guest
{


    public String userID;
    public String name;
    public String picURI;
    public int Points;
    public boolean isDJ = false;

    public Guest()
    {
        Points = 0;
    }

    public Guest(String _userid, String _name, String _picURI)
    {
        userID = _userid;
        name = _name;
        picURI = _picURI;
        Points = 0;
    }


}
