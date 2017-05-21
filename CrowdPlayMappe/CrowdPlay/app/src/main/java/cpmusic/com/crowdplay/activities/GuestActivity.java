package cpmusic.com.crowdplay.activities;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import cpmusic.com.crowdplay.R;
import cpmusic.com.crowdplay.adapters.RecycleViewAdapter;
import cpmusic.com.crowdplay.model.firebaseModel.Guest;
import cpmusic.com.crowdplay.model.firebaseModel.Track;
import cpmusic.com.crowdplay.util.NetworkChecker;
import cpmusic.com.crowdplay.util.SharedPreferencesData;

public class GuestActivity extends AppCompatActivity {

    NetworkChecker networkChecker;

    RecycleViewAdapter adapter;

    RecyclerView recyclerView;

    FirebaseDatabase database;
    DatabaseReference mTracksRef;
    DatabaseReference mPartyRef;
    DatabaseReference mGuestsRef;

    String partyID;

    FloatingActionButton fabSearch;

    SharedPreferencesData sharedPreferencesData;
    String UserID;
    boolean Allreadyloggedin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest);

        // SHaredPreferences
        sharedPreferencesData = new SharedPreferencesData();
        UserID = sharedPreferencesData.getFacebookUID(this);
        Allreadyloggedin = false;

        Intent partyIntent = getIntent();
        partyID = partyIntent.getStringExtra("ID");

        networkChecker = new NetworkChecker();

        fabSearch = (FloatingActionButton)findViewById(R.id.fabSearch);
        fabSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GuestActivity.this, SearchActivity.class);
                intent.putExtra("ID", partyID);
                startActivity(intent);
            }
        });

        database = FirebaseDatabase.getInstance();

        mPartyRef = database.getReference().child(partyID);
        mTracksRef = mPartyRef.child("Tracks");

        mTracksRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                Track newTrack = dataSnapshot.getValue(Track.class);
                adapter.addTrack(newTrack);

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s)
            {
                Track track = dataSnapshot.getValue(Track.class);
                adapter.changeVote(track);

                if (track.Votes == 0)
                {
                    adapter.moveTrackToLast(track);
                }
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

        setUpRecyclerView();
        addGuestToParty();
    }


    private void setUpRecyclerView() {

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        adapter = new RecycleViewAdapter(this, mPartyRef);
        recyclerView.setAdapter(adapter);

        LinearLayoutManager mLinearLayoutManagerVertical = new LinearLayoutManager(this); // (Context context, int spanCount)
        mLinearLayoutManagerVertical.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(mLinearLayoutManagerVertical);

        recyclerView.getItemAnimator().setChangeDuration(400);
        recyclerView.getItemAnimator().setMoveDuration(300);
        recyclerView.getItemAnimator().setRemoveDuration(200);
        recyclerView.getItemAnimator().setAddDuration(300);
    }


    private void addGuestToParty()
    {
        mGuestsRef = mPartyRef.child("Guests");

        mGuestsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot i:dataSnapshot.getChildren())
                {
                    Guest g = dataSnapshot.child(i.getKey()).getValue(Guest.class);
                    if(g.getUserID().equals(UserID))
                    {
                       Allreadyloggedin = true;
                    }
                }
                if(Allreadyloggedin == false)
                {
                    String thisUserName = sharedPreferencesData.getFacebookFullName(GuestActivity.this);
                    String thisUserID = sharedPreferencesData.getFacebookUID(GuestActivity.this);
                    mGuestsRef.push().setValue(new Guest(thisUserID,thisUserName));
                    Allreadyloggedin = true;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
