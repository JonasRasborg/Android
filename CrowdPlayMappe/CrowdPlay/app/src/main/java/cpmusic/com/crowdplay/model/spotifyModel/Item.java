package cpmusic.com.crowdplay.model.spotifyModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Jonas R. Hartogsohn on 15-05-2017.
 */

public class Item {


    @SerializedName("album")
    @Expose
    public Album album;
    @SerializedName("artists")
    @Expose
    public List<Artist_> artists = null;
    @SerializedName("available_markets")
    @Expose
    public List<String> availableMarkets = null;
    @SerializedName("disc_number")
    @Expose
    public Integer discNumber;
    @SerializedName("duration_ms")
    @Expose
    public Integer durationMs;
    @SerializedName("explicit")
    @Expose
    public Boolean explicit;
    @SerializedName("external_ids")
    @Expose
    public ExternalIds externalIds;
    @SerializedName("external_urls")
    @Expose
    public ExternalUrls___ externalUrls;
    @SerializedName("href")
    @Expose
    public String href;
    @SerializedName("id")
    @Expose
    public String id;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("popularity")
    @Expose
    public Integer popularity;
    @SerializedName("preview_url")
    @Expose
    public String previewUrl;
    @SerializedName("track_number")
    @Expose
    public Integer trackNumber;
    @SerializedName("type")
    @Expose
    public String type;
    @SerializedName("uri")
    @Expose
    public String uri;
}
