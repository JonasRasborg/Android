package cpmusic.com.crowdplay.util;

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

    public FirebaseConnector(DatabaseReference db){

        mRootRef = db;
    }

    public void putNewTrack(Track track){
        mRootRef.push().setValue(track);
    }

}
