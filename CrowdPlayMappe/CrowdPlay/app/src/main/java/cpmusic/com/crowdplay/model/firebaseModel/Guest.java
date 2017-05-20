package cpmusic.com.crowdplay.model.firebaseModel;

import com.facebook.FacebookSdk;

/**
 * Created by ander on 19/05/2017.
 */

public class Guest
{


    String userID;
    String name;

    public Guest()
    {

    }

    public Guest(String _userid, String _name)
    {
        userID = _userid;
        name = _name;
    }

    public String getUserID() {
        return userID;
    }

    public String getName() {
        return name;
    }

}
