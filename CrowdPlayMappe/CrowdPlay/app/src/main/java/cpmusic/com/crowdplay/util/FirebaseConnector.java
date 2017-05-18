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

    public FirebaseConnector(DatabaseReference db, Context context){

        mRootRef = db;
        newTrackIntent = new Intent("trackAdded");
        localBroadcastManager = LocalBroadcastManager.getInstance(context);

        mRootRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s)
            {
                Track newTrack = dataSnapshot.getValue(Track.class);
                newTrackIntent.putExtra("newTrack", newTrack);
                localBroadcastManager.sendBroadcast(newTrackIntent);
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


    public void putNewTrack(Track track){
        mRootRef.push().setValue(track);
    }

}
