package cpmusic.com.crowdplay.model.firebaseModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

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

    @SerializedName("votes")
    @Expose
    public int Votes;
}
