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
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.facebook.AccessToken;
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
import java.util.List;

import cpmusic.com.crowdplay.R;
import cpmusic.com.crowdplay.Services.DrakeService;
import cpmusic.com.crowdplay.util.SharedPreferencesConnector;

public class MainActivity extends AppCompatActivity {

    private Location userlocation;
    private LatLng userLatLng;
    private LocationManager locationManager;
    static final int MY_PERMISSIONS_REQUEST_FINE_LOCATION=0;

    // Logging
    private String LOGTAG = MainActivity.class.getSimpleName();

    // Facebook
    CallbackManager mCallbackManager;
    LoginManager loginManager;
    ProfileTracker mProfileTracker;

    // Shared Preferences helper class "SharedPreferencesConnector"
    SharedPreferencesConnector sharedPreferencesConnector;

    FloatingActionButton fabDJ, fabGuest;

    private Intent serviceIntent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       SaveMyLocationLocally(); // saves location in userLatLng member

        serviceIntent = new Intent(MainActivity.this, DrakeService.class);
        startService(serviceIntent);

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


        // Check if user is allready logged in
        if (sharedPreferencesConnector.getLoggedInStatus(this)==false)
        {
            // if not logged in
            LoginFacebook();
        }

    }

    public void OpenPartyFinderActivity()
    {
        if (sharedPreferencesConnector.getLoggedInStatus(this)==true) {
            Intent intent = new Intent(this, PartyFinderActivity.class);
            startActivity(intent);
        }
        else
        {
            Toast.makeText(this, R.string.facebook_login_find_parties, Toast.LENGTH_SHORT).show();
        }
    }

    public void OpenSetupPartyActivity() {
        if (sharedPreferencesConnector.getLoggedInStatus(this) == true) {
            if (userlocation != null) {
                Intent intent = new Intent(this, SetupPartyActivity.class);

                userLatLng = new LatLng(userlocation.getLatitude(), userlocation.getLongitude());

                Bundle args = new Bundle();

                args.putParcelable("Location", userLatLng);
                intent.putExtra("bundle", args);

                startActivity(intent);
            } else {
                Toast.makeText(this, R.string.location_not_found, Toast.LENGTH_SHORT).show();
            }
        }
        else
        {
            Toast.makeText(this, R.string.facebook_login_start_party, Toast.LENGTH_SHORT).show();
        }
    }


    // Invoked when requestPermissions callback returns
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

                    Toast.makeText(this, R.string.location_permissions_needed, Toast.LENGTH_SHORT).show();
                    finish();
                }
                return;
            }

        }
    }

    public void SaveMyLocationLocally()
    {
        if (locationManager == null) {
            locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        }

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
            if(userlocation == null)
            {
                final LocationListener locationListener = new LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {
                        Log.d(LOGTAG,"Locationlistener onLocationChanged");
                        userlocation = location;
                    }

                    @Override
                    public void onStatusChanged(String provider, int status, Bundle extras) {
                        Log.d(LOGTAG,"Locationlistener onStatusChanged");

                    }

                    @Override
                    public void onProviderEnabled(String provider) {
                        Log.d(LOGTAG,"Locationlistener onProviderEnabled");

                    }

                    @Override
                    public void onProviderDisabled(String provider) {
                        Log.d(LOGTAG,"Locationlistener onProviderDisabled");

                    }
                };
                Looper looper = Looper.getMainLooper();

                String someprovider = LocationManager.GPS_PROVIDER;
                locationManager.requestSingleUpdate(someprovider,locationListener,looper);
            }
        }
    }

    // Code example for facebook login found at
    // https://stackoverflow.com/questions/29634660/facebook-sdk-4-0-1-login-without-login-button &
    // https://stackoverflow.com/questions/29642759/profile-getcurrentprofile-returns-null-after-logging-in-fb-api-v4-0
    public void LoginFacebook()
    {

        // Try to log user in with facebook
        List<String> permissionNeeds= Arrays.asList("public_profile");
        mCallbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().logInWithReadPermissions(this,permissionNeeds);

        loginManager.getInstance().registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Toast.makeText(MainActivity.this, R.string.login_success, Toast.LENGTH_SHORT).show();

              // Even if login was succesfull, .getCurrentProfile may return null. Start Listening for profile Changed instead.
                if(Profile.getCurrentProfile()==null)
                {

                    mProfileTracker=new ProfileTracker() {
                        @Override
                        protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {
                            Log.d(LOGTAG, "onCurrentProfileChanged");
                            Profile p = currentProfile;
                            sharedPreferencesConnector.setFacebookUID(MainActivity.this,p.getId());
                            sharedPreferencesConnector.setFacebookFirstName(MainActivity.this,p.getFirstName());
                            sharedPreferencesConnector.setFacebookProfilePicURI(MainActivity.this,p.getProfilePictureUri(200,200).toString());
                            sharedPreferencesConnector.setFacebookFullName(MainActivity.this,p.getName());
                            sharedPreferencesConnector.setLoggidInStatus(MainActivity.this,true);

                            mProfileTracker.stopTracking();
                            Log.d(LOGTAG,sharedPreferencesConnector.getFacebookFirstName(MainActivity.this));
                            Log.d(LOGTAG,sharedPreferencesConnector.getFacebookUID(MainActivity.this));
                            Log.d(LOGTAG,sharedPreferencesConnector.getFacebookFullName(MainActivity.this));
                            Log.d(LOGTAG,sharedPreferencesConnector.getFacebookProfilepicUri(MainActivity.this));
                            Toast.makeText(MainActivity.this,getString(R.string.welcome_facebookname)+" "+sharedPreferencesConnector.getFacebookFirstName(MainActivity.this),Toast.LENGTH_SHORT).show();


                        }
                    };
                    mProfileTracker.startTracking();
                }
                else
                {
                    Log.d(LOGTAG, "Current profile not null, automatic login");
                    Profile p = Profile.getCurrentProfile();

                    sharedPreferencesConnector.setFacebookUID(MainActivity.this,p.getId());
                    sharedPreferencesConnector.setFacebookFirstName(MainActivity.this,p.getFirstName());
                    sharedPreferencesConnector.setFacebookProfilePicURI(MainActivity.this,p.getProfilePictureUri(200,200).toString());
                    sharedPreferencesConnector.setFacebookFullName(MainActivity.this,p.getName());
                    sharedPreferencesConnector.setLoggidInStatus(MainActivity.this,true);


                    Log.d(LOGTAG,sharedPreferencesConnector.getFacebookFirstName(MainActivity.this));
                    Log.d(LOGTAG,sharedPreferencesConnector.getFacebookUID(MainActivity.this));
                    Log.d(LOGTAG,sharedPreferencesConnector.getFacebookFullName(MainActivity.this));
                    Log.d(LOGTAG,sharedPreferencesConnector.getFacebookProfilepicUri(MainActivity.this));

                    Toast.makeText(MainActivity.this,getString(R.string.welcome_facebookname)+" "+sharedPreferencesConnector.getFacebookFirstName(MainActivity.this),Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancel() {
                Toast.makeText(MainActivity.this, getString(R.string.login_cancelled), Toast.LENGTH_SHORT).show();
                Log.d(LOGTAG, "Login Cancelled");
                sharedPreferencesConnector.setLoggidInStatus(MainActivity.this,false);
                Toast.makeText(MainActivity.this, getString(R.string.need_facebook_login), Toast.LENGTH_SHORT).show();
                finish();

            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(MainActivity.this, getString(R.string.login_error), Toast.LENGTH_SHORT).show();
                Log.d(LOGTAG, "Login Error");
                sharedPreferencesConnector.setLoggidInStatus(MainActivity.this,false);
                Toast.makeText(MainActivity.this, getString(R.string.need_facebook_login), Toast.LENGTH_SHORT).show();
                finish();


            }
        });
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Pass the activity result back to the Facebook SDK
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

}


