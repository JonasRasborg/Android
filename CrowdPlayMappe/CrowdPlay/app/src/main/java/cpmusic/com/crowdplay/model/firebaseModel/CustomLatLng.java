package cpmusic.com.crowdplay.model.firebaseModel;

/**
 * Created by ander on 18/05/2017.
 */

public class CustomLatLng {
    double latitude;
    double longtitude;

    public CustomLatLng()
    {

    }

    public CustomLatLng(double _lat, double _long)
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
