package cpmusic.com.crowdplay.activities;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import cpmusic.com.crowdplay.R;
import cpmusic.com.crowdplay.model.firebaseModel.Party;

public class PartyFinderActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    LatLng navitas = new LatLng(56.158897, 10.213706);
    LatLng party1 = new LatLng(56.1587, 10.213);
    static final int MY_PERMISSIONS_REQUEST_FINE_LOCATION=0;
    int minTime = 500; // millisecs
    int minDistance = 1; // meters
    LocationManager locationManager;




    // Firebase database instances
    private DatabaseReference mDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_party_finder);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // Check if app has permission to use Location (if FINE is permittes, so is COARSE)
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // If not, ask for it
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},MY_PERMISSIONS_REQUEST_FINE_LOCATION);
            // onRequestPermissionsResult is automatically invoked now
        }

    }



    // When map async call returns (map is built on screen)
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this,R.raw.partymapstyle));


        // put user location on map
        PutMylocationOnMap();

        // Start listening for user location updates - if changed enough map is updated
        ListenForUserLocationUpdates();

      

        // Firebase get all parties

        mDatabase = FirebaseDatabase.getInstance().getReference();

        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot i:dataSnapshot.getChildren())
                {
                    Party p = dataSnapshot.child(i.getKey()).getValue(Party.class);
                    LatLng thisLatLng = new LatLng(p.location.getLatitude(),p.location.getLongtitude());
                    Marker marker = mMap.addMarker(new MarkerOptions().position(thisLatLng).title(p.name).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                    // Tilføjer database key key til Markers "Tag"
                    marker.setTag(dataSnapshot.child(i.getKey()).getKey());

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        // Listener for marker (Pin) click
       /* mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {

                Intent intent = new Intent(PartyFinderActivity.this,GuestActivity.class);

                intent.putExtra("ID",marker.getTag().toString());
                startActivity(intent);
                return true;
            }
        });*/

        //Listener for marker inforWindow click

        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(final Marker marker) {

                // Alert dialog for entering party password
                AlertDialog.Builder alert = new AlertDialog.Builder(PartyFinderActivity.this);

                alert.setMessage("Enter password for Party");

                final EditText passwordEditText = new EditText(PartyFinderActivity.this);

                alert.setView(passwordEditText);

                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(PartyFinderActivity.this, "If you dont know the password, ask your host for it", Toast.LENGTH_SHORT).show();
                    }
                });


                alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String correctPassword;
                        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                String correctPassword = dataSnapshot.child(marker.getTag().toString()).child("password").getValue(String.class);
                                String typedPassword = passwordEditText.getText().toString();

                                if (correctPassword.equals(typedPassword))
                                {
                                    Intent intent = new Intent(PartyFinderActivity.this,GuestActivity.class);
                                    intent.putExtra("PartyKey",marker.getTag().toString());
                                    startActivity(intent);
                                }
                                else
                                {
                                    Toast.makeText(PartyFinderActivity.this, "Incorrect Password", Toast.LENGTH_SHORT).show();
                                }

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });


                    }
                });



                alert.show();





            }
        });






    }







    public void ListenForUserLocationUpdates()
    {
        if (locationManager == null) {
            locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        }

        if (locationManager != null) {
            try {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, minTime, minDistance, locationListener);

            } catch (SecurityException ex) {
                //TODO: user have disabled location permission - need to validate this permission for newer versions
            }
        } else {

        }
    }

    public void PutMylocationOnMap()
    {
        // Cheking if app has permission to get users location
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            // If not, ask for it
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},MY_PERMISSIONS_REQUEST_FINE_LOCATION);
            // onRequestPermissionsResult is automatically invoked now

        }
        else {
            mMap.setMyLocationEnabled(true);
        }


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

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    PutMylocationOnMap();


                } else {

                    Toast.makeText(this, "Cant use the app without allowing permissions to location", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    // Lytter på når GPS location ændres (væsentligt)
    private LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {

            // Update user location
            LatLng userLatLng = new LatLng(location.getLatitude(),location.getLongitude());
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLatLng,15));
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };
}
