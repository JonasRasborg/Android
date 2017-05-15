package hartogsohn.crowdplayapitest.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Jonas R. Hartogsohn on 15-05-2017.
 */

public class Example {

    @SerializedName("artists")
    @Expose
    public Artists artists;
    @SerializedName("tracks")
    @Expose
    public Tracks tracks;
}
