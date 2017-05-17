package cpmusic.com.crowdplay.model.spotifyModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Jonas R. Hartogsohn on 15-05-2017.
 */

public class Example  implements Serializable {

    @SerializedName("artists")
    @Expose
    public Artists artists;
    @SerializedName("tracks")
    @Expose
    public Tracks tracks;
}
