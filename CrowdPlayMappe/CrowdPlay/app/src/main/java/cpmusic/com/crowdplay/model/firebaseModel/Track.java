package cpmusic.com.crowdplay.model.firebaseModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by Jonas R. Hartogsohn on 17-05-2017.
 */

public class Track implements Serializable
{
    @SerializedName("title")
    @Expose
    public String Title;

    @SerializedName("artist")
    @Expose
    public String Artist;

    @SerializedName("album")
    @Expose
    public String Album;

    @SerializedName("image")
    @Expose
    public String ImageURL;

    @SerializedName("uri")
    @Expose
    public String URI;

    @SerializedName("voters")
    @Expose
    public HashMap<String,Guest> Voters;

    public boolean isVoted;


    public String AddedBy;


    public boolean IsPlaying = false;

}
