package cpmusic.com.crowdplay.activities;

import android.Manifest;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Looper;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
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
import com.spotify.sdk.android.player.Spotify;

import cpmusic.com.crowdplay.R;
import cpmusic.com.crowdplay.model.firebaseModel.Party;

public class PartyFinderActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    static final int MY_PERMISSIONS_REQUEST_FINE_LOCATION=0;
    LocationManager locationManager;
    int MAPZOOMLEVEL = 16;
    String LOGTAG = PartyFinderActivity.class.getSimpleName();
    private Location userlocation;
    private boolean mapready = false;

    Context mContext;

    // Firebase database instances
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_party_finder);

        mContext = this;
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
        mapready = true;
        mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this,R.raw.partymapstyle));

        if (locationManager == null) {
            locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        }
        // put user location on map
        PutMylocationOnMap();


        // Firebase get all parties

        mDatabase = FirebaseDatabase.getInstance().getReference();



        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mMap.clear();
                for(DataSnapshot i:dataSnapshot.getChildren())
                {

                    Party p = dataSnapshot.child(i.getKey()).getValue(Party.class);
                    if(p.Active==true) {
                        LatLng thisLatLng = new LatLng(p.location.getLatitude(),p.location.getLongtitude());
                        Marker marker = mMap.addMarker(new MarkerOptions().position(thisLatLng).title(p.name).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                        // Tilføjer database key key til Markers "Tag"
                        marker.setTag(dataSnapshot.child(i.getKey()).getKey());
                    }

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        //Listener for marker inforWindow click

        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(final Marker marker) {

                LayoutInflater inflater = LayoutInflater.from(mContext);

                View viewDialog = inflater.inflate(R.layout.type_party_password, null);
                final Dialog passDialog = new Dialog(mContext);
                passDialog.setContentView(viewDialog);

                final EditText editPassword = (EditText) viewDialog.findViewById(R.id.editPassword);
                Button btnCancel = (Button) viewDialog.findViewById(R.id.btnCancel);
                Button btnOK = (Button) viewDialog.findViewById(R.id.btnOK);

                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(PartyFinderActivity.this, getString(R.string.ask_for_password), Toast.LENGTH_SHORT).show();
                        passDialog.dismiss();
                    }
                });

                btnOK.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String correctPassword;
                        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                if (marker.getTag() != null) {
                                    String markterTag = marker.getTag().toString();


                                    String correctPassword = dataSnapshot.child(markterTag).child("password").getValue(String.class);
                                    String typedPassword = editPassword.getText().toString();

                                    if (correctPassword.equals(typedPassword)) {
                                        Intent intent = new Intent(PartyFinderActivity.this, GuestActivity.class);
                                        intent.putExtra("PartyKey", marker.getTag().toString());
                                        startActivityForResult(intent, 200);
                                    } else {
                                        Toast.makeText(PartyFinderActivity.this, getString(R.string.incorrect_password), Toast.LENGTH_SHORT).show();
                                    }
                                }

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                        passDialog.dismiss();
                    }
                });

                passDialog.show();

            }
        });}


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

            if(userlocation==null)
            {
                String provider = LocationManager.GPS_PROVIDER;
                userlocation = locationManager.getLastKnownLocation(provider);
                Log.d(LOGTAG,"LocationManager: location was found with GPS provider");

            }
            if(userlocation==null)
            {
                String provider = LocationManager.NETWORK_PROVIDER;
                userlocation = locationManager.getLastKnownLocation(provider);
                Log.d(LOGTAG,"LocationManager: location was found with Network provider");

            }
            if(userlocation==null)
            {
                String provider = LocationManager.PASSIVE_PROVIDER;
                userlocation = locationManager.getLastKnownLocation(provider);
                Log.d(LOGTAG,"LocationManager: location was found with Passive provider");
            }
            if(userlocation!=null)
            {
                LatLng userLatLng = new LatLng(userlocation.getLatitude(),userlocation.getLongitude());
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLatLng,MAPZOOMLEVEL));
            }

        }


    }

    @Override
    protected void onStart() {
        super.onStart();
        if(mapready == true)
        {
            PutMylocationOnMap();
        }
    }
    @Override
    protected void onStop() {
        super.onStop();
        // Cheking if app has permission to get users location
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            // If not, ask for it
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},MY_PERMISSIONS_REQUEST_FINE_LOCATION);
            // onRequestPermissionsResult is automatically invoked now
        }

        else {

            mMap.setMyLocationEnabled(false);
        }

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapready = false;
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

                    // If permission granted
                    PutMylocationOnMap();


                } else {

                    Toast.makeText(this, getString(R.string.allow_location), Toast.LENGTH_SHORT).show();
                }
                return;
            }


        }
    }

}
