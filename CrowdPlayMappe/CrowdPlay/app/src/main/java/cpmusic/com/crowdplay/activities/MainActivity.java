package cpmusic.com.crowdplay.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.model.LatLng;

import cpmusic.com.crowdplay.R;

public class MainActivity extends AppCompatActivity {

    private Location userlocation;
    private LatLng userLatLng;
    private LocationManager locationManager;
    static final int MY_PERMISSIONS_REQUEST_FINE_LOCATION=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (locationManager == null) {
            locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        }

       SaveMyLocationLocally(); // saves location in userlocation member


        Button BtnDJ = (Button) findViewById(R.id.BtnDJ);
        Button BtnGuest = (Button) findViewById(R.id.BtnGuest);

        BtnDJ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                OpenSetupPartyActivity();
            }
        });

        BtnGuest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenPartyFinderActivity();
            }
        });

    }

    public void OpenPartyFinderActivity()
    {
        Intent intent = new Intent(this,PartyFinderActivity.class);
        startActivity(intent);
    }

    public void OpenSetupPartyActivity()
    {
        if (userlocation!=null) {
            Intent intent = new Intent(this, SetupPartyActivity.class);

            userLatLng = new LatLng(userlocation.getLatitude(),userlocation.getLongitude());

            Bundle args = new Bundle();

            args.putParcelable("Location",userLatLng);
            intent.putExtra("Location", args);

            startActivity(intent);
        }
        else
            Toast.makeText(this,"Sorry, we could not find your location",Toast.LENGTH_SHORT).show();
    }

    // Invokes når der kommer svar på requestPermissions
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    SaveMyLocationLocally();

                } else {

                    Toast.makeText(this, "Cant use the app without allowing permissions to location", Toast.LENGTH_SHORT).show();
                    finish();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    public void SaveMyLocationLocally()
    {
        //Check for permission on location services
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            // If not, ask for it
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},MY_PERMISSIONS_REQUEST_FINE_LOCATION);
            // onRequestPermissionsResult is automatically invoked now

        }
        else {

            String GPSprovider = LocationManager.GPS_PROVIDER;
            userlocation = locationManager.getLastKnownLocation(GPSprovider);
            if(userlocation==null)
            {
                String Networkprovider = LocationManager.NETWORK_PROVIDER;
                userlocation = locationManager.getLastKnownLocation(Networkprovider);
            }
        }
    }


}


