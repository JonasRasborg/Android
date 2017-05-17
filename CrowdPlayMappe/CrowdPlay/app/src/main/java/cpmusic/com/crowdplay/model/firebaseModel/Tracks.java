package cpmusic.com.crowdplay.model.firebaseModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by rrask on 17-05-2017.
 */

public class Tracks implements Serializable
{
    @SerializedName("tracks")
    @Expose
    public List<Track> tracks;
}
