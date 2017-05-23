package cpmusic.com.crowdplay.activities;

import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
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
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import cpmusic.com.crowdplay.adapters.PlaylistAdapter;
import cpmusic.com.crowdplay.model.firebaseModel.Track;


import cpmusic.com.crowdplay.R;

public class DJActivity extends AppCompatActivity implements SpotifyPlayer.NotificationCallback, ConnectionStateCallback {

    private static final String TAG = DJActivity.class.getSimpleName();
    private static final String CLIENT_ID = "1c078fda78264a8e8372cc576a259a1a";
    private static final String REDIRECT_URI = "crowdplay://callback";
    private static final int REQUEST_CODE = 1337;

    FirebaseDatabase database;

    DatabaseReference mTracksRef;
    DatabaseReference mPartyRef;
    Context mContext;

    ArrayList<Track> newTracks;
    boolean startedFirstTrack = false;

    int timer = 0;

    Track topTrack;
    Track nowPlaying;

    ToggleButton togglePlay;
    ProgressBar progressBar;
    CountDownTimer trackCountDownTimer;
    private Player mPlayer;

    boolean firstTrackStarted = false;

    private Bundle bundle;
    private TextView txtArtist, txtTrack;
    private ImageView imgAlbum;

    String partyKey;

    RecyclerView recyclerView;
    PlaylistAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dj);

        Log.i(TAG,"OnCreate");

        //LocalBroadcastManager.getInstance(this).registerReceiver(mReceiveFromFirebase, new IntentFilter("trackAdded"));
        //LocalBroadcastManager.getInstance(this).registerReceiver(mReceiveAllTracks, new IntentFilter("allTracks"));
        newTracks = new ArrayList<Track>();

        bundle = getIntent().getExtras();
        partyKey = bundle.getString("PartyKey");

        database = FirebaseDatabase.getInstance();
        mPartyRef = database.getReference().child(partyKey);
        mTracksRef = database.getReference(partyKey).child("Tracks");

        txtArtist = (TextView)findViewById(R.id.txtArtist);
        txtTrack = (TextView)findViewById(R.id.txtTrack);
        imgAlbum = (ImageView)findViewById(R.id.imgAlbum);
        mContext = this;

        progressBar = (ProgressBar)findViewById(R.id.progressBar);

        togglePlay = (ToggleButton)findViewById(R.id.togglePlay);
        togglePlay.setChecked(true);
        togglePlay.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(mPlayer!=null){
                    if(isChecked){
                        if(mPlayer.getPlaybackState().isActiveDevice){
                            mPlayer.resume(null);
                        }
                    }
                    else{
                        mPlayer.pause(null);
                    }
                }
            }
        });

        //Spotify SDK:
        AuthenticationRequest.Builder builder = new AuthenticationRequest.Builder(CLIENT_ID, AuthenticationResponse.Type.TOKEN, REDIRECT_URI);
        builder.setScopes(new String[]{"user-read-private", "streaming"});
        AuthenticationRequest request = builder.build();
        AuthenticationClient.openLoginActivity(this, REQUEST_CODE, request);

        setUpRecyclerView();
        mPartyRef.child("Active").setValue(true);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (adapter.getTopTrack() != null)
        {
            adapter.addPlayingSong(topTrack);
        }
        Spotify.destroyPlayer(this);
        mPartyRef.child("Active").setValue(false);
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
        setUpListeners();
    }

    public void startFirstSong(){
        trackCountDownTimer = new CountDownTimer(3000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                playTopSong();
            }
        };
        trackCountDownTimer.start();
    }

    public void setupProgressBar(long ms){
        progressBar.setProgress(timer);
        progressBar.setMax(1000);
        trackCountDownTimer = new CountDownTimer(ms,ms/1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timer++;
                progressBar.setProgress(timer);
            }

            @Override
            public void onFinish() {

            }

        };
        trackCountDownTimer.start();
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

            case kSpPlaybackNotifyTrackChanged:
                if(!mPlayer.getPlaybackState().isPlaying)
                {
                    adapter.addPlayingSong(topTrack);

                    playTopSong();

                    break;
                }

                else{
                    timer = 0;
                    setupProgressBar(mPlayer.getMetadata().currentTrack.durationMs);
                }

            case kSpPlaybackNotifyPause:
                trackCountDownTimer.cancel();
                break;
            case kSpPlaybackNotifyPlay:
                trackCountDownTimer.start();



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

    public void playTopSong(){
        topTrack = adapter.getTopTrack();
        mPlayer.playUri(null,topTrack.URI,0,0);
        adapter.resetVotes(topTrack);
        recyclerView.scrollToPosition(0);
        txtArtist.setText(topTrack.Artist);
        txtTrack.setText(topTrack.Title);
        Picasso.with(this).load(topTrack.ImageURL).into(imgAlbum);
    }

    public void setUpListeners(){
        mTracksRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Track newTrack = dataSnapshot.getValue(Track.class);
                adapter.addTrack(newTrack);
                if(mPlayer != null && !startedFirstTrack){
                    startedFirstTrack = true;
                    startFirstSong();
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Track track = dataSnapshot.getValue(Track.class);
                adapter.changeVote(track);
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

    private void setUpRecyclerView() {

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        adapter = new PlaylistAdapter(this, mPartyRef);
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
