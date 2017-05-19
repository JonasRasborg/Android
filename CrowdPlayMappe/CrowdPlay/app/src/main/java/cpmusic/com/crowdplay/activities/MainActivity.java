package cpmusic.com.crowdplay.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.model.LatLng;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;

import cpmusic.com.crowdplay.R;
import cpmusic.com.crowdplay.model.firebaseModel.FacebookProfile;

public class MainActivity extends AppCompatActivity {

    private Location userlocation;
    private LatLng userLatLng;
    private LocationManager locationManager;
    static final int MY_PERMISSIONS_REQUEST_FINE_LOCATION=0;

    // Facebook
    CallbackManager mCallbackManager;

    // Firebase
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        if (locationManager == null) {
            locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        }

       SaveMyLocationLocally(); // saves location in userLatLng member


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

        LoginFacebook();





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

    public void LoginFacebook()
    {
        FacebookSdk.sdkInitialize(this.getApplicationContext());
        mCallbackManager = CallbackManager.Factory.create();

        LoginManager.getInstance().registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                Toast.makeText(MainActivity.this,"User ID: "+loginResult.getAccessToken().getUserId(),Toast.LENGTH_LONG).show();
                handleFacebookAccessToken(loginResult.getAccessToken());


            }

            @Override
            public void onCancel() {

                Toast.makeText(MainActivity.this,"You cant use this app without logging into FaceBook",Toast.LENGTH_LONG).show();
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

    private void handleFacebookAccessToken(AccessToken token) {

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(MainActivity.this,"Firebase authentication failed",Toast.LENGTH_LONG).show();

                        }

                        // ...
                    }
                });
    }



}


