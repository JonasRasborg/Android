package cpmusic.com.crowdplay.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Looper;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.maps.model.LatLng;

import com.facebook.FacebookSdk;
import com.google.firebase.database.DatabaseReference;

import java.util.Arrays;

import cpmusic.com.crowdplay.R;
import cpmusic.com.crowdplay.util.SharedPreferencesConnector;

public class MainActivity extends AppCompatActivity {

    private Location userlocation;
    private LatLng userLatLng;
    private LocationManager locationManager;
    static final int MY_PERMISSIONS_REQUEST_FINE_LOCATION=0;

    // Facebook
    CallbackManager mCallbackManager;
    ProfileTracker mProfileTracker;
    Profile currentProfile;

    // Shared Preferences helper class "SharedPreferencesConnector"

    SharedPreferencesConnector sharedPreferencesConnector;

    FloatingActionButton fabDJ, fabGuest;



    // Firebase
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        if (locationManager == null) {
            locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        }

       SaveMyLocationLocally(); // saves location in userLatLng member


        fabDJ = (FloatingActionButton)findViewById(R.id.fabDJ);
        fabGuest = (FloatingActionButton)findViewById(R.id.fabGuest);

        fabDJ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenSetupPartyActivity();
            }
        });


        fabGuest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenPartyFinderActivity();
            }
        });



        sharedPreferencesConnector = new SharedPreferencesConnector();

        currentProfile = Profile.getCurrentProfile();

        // Check if user is allready logged in

        if (currentProfile==null)
        {
            // if not
            LoginFacebook();
        }
        if (currentProfile!=null)
        {
            // if logged in
            Toast.makeText(this,"Welcome back "+currentProfile.getFirstName(),Toast.LENGTH_SHORT).show();
        }
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
            intent.putExtra("bundle", args);

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

        }
    }

    public void SaveMyLocationLocally()
    {
        //Check for permission on location services
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            // If not permitted, ask for it
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},MY_PERMISSIONS_REQUEST_FINE_LOCATION);
            // onRequestPermissionsResult is automatically invoked now

        }
        else {

            if(userlocation==null)
            {
                String provider = LocationManager.GPS_PROVIDER;
                userlocation = locationManager.getLastKnownLocation(provider);
            }
            if(userlocation==null)
            {
                String provider = LocationManager.NETWORK_PROVIDER;
                userlocation = locationManager.getLastKnownLocation(provider);
            }
            if(userlocation==null)
            {
                String provider = LocationManager.PASSIVE_PROVIDER;
                userlocation = locationManager.getLastKnownLocation(provider);
            }
            if(userlocation == null)
            {
                final LocationListener locationListener = new LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {
                        Toast.makeText(MainActivity.this,"We found your location",Toast.LENGTH_LONG).show();
                        userlocation = location;
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
                Looper looper = Looper.getMainLooper();

                String someprovider = LocationManager.GPS_PROVIDER;
                locationManager.requestSingleUpdate(someprovider,locationListener,looper);
            }
        }
    }

    public void LoginFacebook()
    {
        FacebookSdk.sdkInitialize(this.getApplicationContext());
        mCallbackManager = CallbackManager.Factory.create();

        LoginManager.getInstance().registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                mProfileTracker = new ProfileTracker() {
                    @Override
                    protected void onCurrentProfileChanged(Profile oldProfile, Profile newProfile) {

                        sharedPreferencesConnector.setLoggidInStatus(MainActivity.this,true);
                        sharedPreferencesConnector.setFacebookUID(MainActivity.this,newProfile.getId());
                        sharedPreferencesConnector.setFacebookFirstName(MainActivity.this,newProfile.getFirstName());
                        sharedPreferencesConnector.setFacebookFullName(MainActivity.this,newProfile.getName());
                        sharedPreferencesConnector.setFacebookProfilePicURI(MainActivity.this,newProfile.getProfilePictureUri(200,200).toString());
                        Toast.makeText(MainActivity.this,"Welcome "+newProfile.getFirstName(),Toast.LENGTH_SHORT).show();

                    }

                };
            }

            @Override
            public void onCancel() {

                Toast.makeText(MainActivity.this,"You cant use this app without logging into Facebook",Toast.LENGTH_LONG).show();
                finish();
            }

            @Override
            public void onError(FacebookException error) {

            }
        });

        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "user_friends"));

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Pass the activity result back to the Facebook SDK
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

}


