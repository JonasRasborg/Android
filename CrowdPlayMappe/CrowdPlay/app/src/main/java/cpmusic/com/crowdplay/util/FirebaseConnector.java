package cpmusic.com.crowdplay.util;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.Adapter;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.internal.bind.ReflectiveTypeAdapterFactory;

import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantLock;

import cpmusic.com.crowdplay.adapters.PlayListAdapter;
import cpmusic.com.crowdplay.model.firebaseModel.Party;
import cpmusic.com.crowdplay.model.firebaseModel.Track;
import cpmusic.com.crowdplay.model.firebaseModel.Tracks;

/**
 * Created by rrask on 17-05-2017.
 */

public class FirebaseConnector {

    DatabaseReference mRootRef;

    DatabaseReference mChildTracks;

    private Intent newTrackIntent;

    private LocalBroadcastManager localBroadcastManager;


    public FirebaseConnector(DatabaseReference db, Context context){

        mRootRef = db;
        mChildTracks = mRootRef.child("-KkQnOjYOZWhdkxt2lpB").child("Tracks");

        newTrackIntent = new Intent("trackAdded");
        localBroadcastManager = LocalBroadcastManager.getInstance(context);

        mRootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        mChildTracks.addChildEventListener(new ChildEventListener() {
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
        mChildTracks.push().setValue(track);
    }

}
