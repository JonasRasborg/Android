package cpmusic.com.crowdplay.model.firebaseModel;

import com.facebook.FacebookSdk;

/**
 * Created by ander on 19/05/2017.
 */

public class FacebookProfile
{
    String userID;
    String name;
    String profilepicURI;

    public FacebookProfile()
    {

    }

    public FacebookProfile(String _userid, String _name, String _profilepicURI)
    {
        userID = _userid;
        name = _name;
        profilepicURI = _profilepicURI;
    }
}
