package hartogsohn.crowdplayapitest.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Jonas R. Hartogsohn on 15-05-2017.
 */

public class Artist_ {

    @SerializedName("external_urls")
    @Expose
    public ExternalUrls__ externalUrls;
    @SerializedName("href")
    @Expose
    public String href;
    @SerializedName("id")
    @Expose
    public String id;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("type")
    @Expose
    public String type;
    @SerializedName("uri")
    @Expose
    public String uri;
}
