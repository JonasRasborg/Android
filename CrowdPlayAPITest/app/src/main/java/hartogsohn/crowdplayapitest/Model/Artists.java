package hartogsohn.crowdplayapitest.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Jonas R. Hartogsohn on 15-05-2017.
 */

public class Artists {


    @SerializedName("href")
    @Expose
    public String href;
    @SerializedName("items")
    @Expose
    public List<Object> items = null;
    @SerializedName("limit")
    @Expose
    public Integer limit;
    @SerializedName("next")
    @Expose
    public Object next;
    @SerializedName("offset")
    @Expose
    public Integer offset;
    @SerializedName("previous")
    @Expose
    public Object previous;
    @SerializedName("total")
    @Expose
    public Integer total;
}
