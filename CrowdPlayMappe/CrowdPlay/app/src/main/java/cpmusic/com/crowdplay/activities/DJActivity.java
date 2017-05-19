package cpmusic.com.crowdplay.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ToggleButton;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;
import com.spotify.sdk.android.player.Config;
import com.spotify.sdk.android.player.ConnectionStateCallback;
import com.spotify.sdk.android.player.Error;
import com.spotify.sdk.android.player.Player;
import com.spotify.sdk.android.player.PlayerEvent;
import com.spotify.sdk.android.player.Spotify;
import com.spotify.sdk.android.player.SpotifyPlayer;
import java.util.ArrayList;
import cpmusic.com.crowdplay.adapters.RecycleViewAdapter;
import cpmusic.com.crowdplay.model.firebaseModel.Track;


import cpmusic.com.crowdplay.R;
import cpmusic.com.crowdplay.model.firebaseModel.Tracks;

public class DJActivity extends AppCompatActivity implements SpotifyPlayer.NotificationCallback, ConnectionStateCallback {

    private static final String TAG = DJActivity.class.getSimpleName();
    private static final String CLIENT_ID = "1c078fda78264a8e8372cc576a259a1a";
    private static final String REDIRECT_URI = "crowdplay://callback";
    private static final int REQUEST_CODE = 1337;

    DatabaseReference localDB;

    FirebaseDatabase database;

    ArrayList<Track> newTracks;

    ToggleButton togglePlay;

    private Player mPlayer;

    private Tracks tracks;

    RecyclerView recyclerView;
    RecycleViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dj);

        Log.i(TAG,"OnCreate");

        //LocalBroadcastManager.getInstance(this).registerReceiver(mReceiveFromFirebase, new IntentFilter("trackAdded"));
        //LocalBroadcastManager.getInstance(this).registerReceiver(mReceiveAllTracks, new IntentFilter("allTracks"));
        tracks = new Tracks();
        tracks.tracks = new ArrayList<>();
        newTracks = new ArrayList<Track>();

        //Spotify SDK:
        AuthenticationRequest.Builder builder = new AuthenticationRequest.Builder(CLIENT_ID, AuthenticationResponse.Type.TOKEN, REDIRECT_URI);
        builder.setScopes(new String[]{"user-read-private", "streaming"});
        AuthenticationRequest request = builder.build();
        AuthenticationClient.openLoginActivity(this, REQUEST_CODE, request);

        setUpRecyclerView();



        database = FirebaseDatabase.getInstance();
        localDB = database.getReference("-KkS3kuJdL4tOH_zDjCR").child("Tracks");

        localDB.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                long value = dataSnapshot.getChildrenCount();
                Log.i(TAG,"Nr. of children: " + value);

                Track newTrack = dataSnapshot.getValue(Track.class);
                //newTracks.add(newTrack);
                adapter.addTrack(newTrack);

                //GenericTypeIndicator<List<Track>> genericTypeIndicator = new GenericTypeIndicator<List<Track>>() {};
                //List<Track> trackList = dataSnapshot.getValue(genericTypeIndicator);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        // Check if result comes from the correct activity
        if (requestCode == REQUEST_CODE) {
            AuthenticationResponse response = AuthenticationClient.getResponse(resultCode, intent);
            if (response.getType() == AuthenticationResponse.Type.TOKEN) {
                Config playerConfig = new Config(this, response.getAccessToken(), CLIENT_ID);
                Spotify.getPlayer(playerConfig, this, new SpotifyPlayer.InitializationObserver() {
                    @Override
                    public void onInitialized(SpotifyPlayer spotifyPlayer) {
                        mPlayer = spotifyPlayer;
                        mPlayer.addConnectionStateCallback(DJActivity.this);
                        mPlayer.addNotificationCallback(DJActivity.this);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        Log.e("TAG", "Could not initialize player: " + throwable.getMessage());
                    }
                });
            }
        }
    }

    @Override
    public void onLoggedIn() {
        Log.d(TAG, "User logged in");

        mPlayer.playUri(null, "spotify:track:33frkbGSt6bbfj2Nqo9i1p", 0, 0);
        mPlayer.setRepeat(null, true);

    }

    @Override
    public void onLoggedOut() {
        Log.d(TAG, "User logged out");
    }

    @Override
    public void onLoginFailed(Error error) {
        Log.d(TAG, "Login failed");
    }

    @Override
    public void onTemporaryError() {
        Log.d(TAG, "Temporary error occurred");
    }

    @Override
    public void onConnectionMessage(String s) {
        Log.d(TAG, "Received connection message: " + s);
    }

    @Override
    public void onPlaybackEvent(PlayerEvent playerEvent) {
        Log.d(TAG, "Playback event received: " + playerEvent.name());
        switch (playerEvent) {

            case kSpPlaybackNotifyTrackDelivered:
                mPlayer.queue(null,adapter.getTopTrack().URI);


            default:
                break;
        }
    }

    @Override
    public void onPlaybackError(Error error) {
        Log.d(TAG, "Playback error received: " + error.name());
        switch (error) {
            // Handle error type as necessary

            default:
                break;
        }
    }

    private void setUpRecyclerView() {

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        adapter = new RecycleViewAdapter(this, newTracks);
        recyclerView.setAdapter(adapter);


        LinearLayoutManager mLinearLayoutManagerVertical = new LinearLayoutManager(this); // (Context context, int spanCount)
        mLinearLayoutManagerVertical.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(mLinearLayoutManagerVertical);

        recyclerView.getItemAnimator().setChangeDuration(400);
        recyclerView.getItemAnimator().setMoveDuration(300);
        recyclerView.getItemAnimator().setRemoveDuration(200);
        recyclerView.getItemAnimator().setAddDuration(300);

        //recyclerView.setItemAnimator(new DefaultItemAnimator()); // Even if we dont use it then also our items shows default animation. #Check Docs
    }
}
