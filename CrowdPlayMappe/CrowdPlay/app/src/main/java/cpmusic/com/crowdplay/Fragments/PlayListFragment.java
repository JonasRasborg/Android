package cpmusic.com.crowdplay.Fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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


public class PlayListFragment extends Fragment
{

    View view;
    Context mContext;
    Activity mActivity;

    NetworkChecker networkChecker;

    RecycleViewAdapter adapter;

    RecyclerView recyclerView;

    FirebaseDatabase database;
    DatabaseReference mTracksRef;
    DatabaseReference mPartyRef;
    DatabaseReference mGuestsRef;

    String partyID;

    SharedPreferencesData sharedPreferencesData;
    String UserID;
    boolean Allreadyloggedin;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_playlist,container,false);

        mContext = getActivity();
        mActivity = getActivity();

        partyID = getArguments().getString("ID");

        // SHaredPreferences
        sharedPreferencesData = new SharedPreferencesData();
        UserID = sharedPreferencesData.getFacebookUID(mContext);
        Allreadyloggedin = false;

        networkChecker = new NetworkChecker();

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

                if (track.Voters.size() == 0)
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

        return view;
    }


    private void setUpRecyclerView() {

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        adapter = new RecycleViewAdapter(mContext, mPartyRef);
        recyclerView.setAdapter(adapter);

        LinearLayoutManager mLinearLayoutManagerVertical = new LinearLayoutManager(mContext); // (Context context, int spanCount)
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
                    String thisUserName = sharedPreferencesData.getFacebookFullName(mContext);
                    String thisUserID = sharedPreferencesData.getFacebookUID(mContext);
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
