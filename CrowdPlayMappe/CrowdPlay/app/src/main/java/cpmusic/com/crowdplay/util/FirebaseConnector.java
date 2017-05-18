package cpmusic.com.crowdplay.util;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantLock;

import cpmusic.com.crowdplay.model.firebaseModel.Party;
import cpmusic.com.crowdplay.model.firebaseModel.Track;
import cpmusic.com.crowdplay.model.firebaseModel.Tracks;

/**
 * Created by rrask on 17-05-2017.
 */

public class FirebaseConnector {

    private String FirebaseRoot = "https://crowdplay-1191b.firebaseio.com/";

    DatabaseReference mRootRef;

    FirebaseDatabase firebaseDatabase;

    private Intent newTrackIntent;

    private LocalBroadcastManager localBroadcastManager;

    ArrayList<Track> newTracks;


    public FirebaseConnector(DatabaseReference db, Context context){

        mRootRef = db;
        newTrackIntent = new Intent("trackAdded");
        localBroadcastManager = LocalBroadcastManager.getInstance(context);
        newTracks = new ArrayList<>();

        mRootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        mRootRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s)
            {
                    Track newTrack = dataSnapshot.getValue(Track.class);

                    newTrackIntent.putExtra("newTrack", newTrack);
                    localBroadcastManager.sendBroadcast(newTrackIntent);

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s)
            {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s)
            {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void putNewParty(Party party)
    {
        mRootRef.push().setValue(party);
    }


    public void putNewTrack(Track track){
        mRootRef.push().setValue(track);
    }

}
