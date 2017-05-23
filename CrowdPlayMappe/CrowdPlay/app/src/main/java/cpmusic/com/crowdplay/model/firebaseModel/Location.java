package cpmusic.com.crowdplay.model.firebaseModel;

/**
 * Created by ander on 18/05/2017.
 */

public class Location {
    double latitude;
    double longtitude;

    public Location()
    {

    }

    public Location(double _lat, double _long)
    {
        latitude = _lat;
        longtitude = _long;
    }

    public double getLatitude()
    {
        return latitude;
    }

    public double getLongtitude()
    {
        return longtitude;
    }
}
