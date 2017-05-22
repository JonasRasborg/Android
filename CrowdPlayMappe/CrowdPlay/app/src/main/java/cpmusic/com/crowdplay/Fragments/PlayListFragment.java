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

import java.util.ArrayList;

import cpmusic.com.crowdplay.R;
import cpmusic.com.crowdplay.adapters.PlaylistAdapter;
import cpmusic.com.crowdplay.adapters.GuestAdapter;
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

    PlaylistAdapter adapter;
    GuestAdapter adapterGuests;

    RecyclerView recyclerView;
    RecyclerView recyclerViewGuests;

    FirebaseDatabase database;
    DatabaseReference mTracksRef;
    DatabaseReference mPartyRef;
    DatabaseReference mGuestsRef;

    String partyID;

    SharedPreferencesData sharedPreferencesData;
    String UserID;
    String facebookPicUri;
    boolean Allreadyloggedin;

    ArrayList<Guest> guests;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_playlist,container,false);

        mContext = getActivity();
        mActivity = getActivity();

        partyID = getArguments().getString("ID");
        guests = new ArrayList<>();

        // SHaredPreferences
        sharedPreferencesData = new SharedPreferencesData();
        UserID = sharedPreferencesData.getFacebookUID(mContext);
        facebookPicUri = sharedPreferencesData.getFacebookProfilepicUri(mContext);

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
        setUpRecyclerViewGuests();
        addGuestToParty();

        return view;
    }


    private void setUpRecyclerView() {

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        adapter = new PlaylistAdapter(mContext, mPartyRef);
        recyclerView.setAdapter(adapter);

        LinearLayoutManager mLinearLayoutManagerVertical = new LinearLayoutManager(mContext); // (Context context, int spanCount)
        mLinearLayoutManagerVertical.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(mLinearLayoutManagerVertical);

        recyclerView.getItemAnimator().setChangeDuration(400);
        recyclerView.getItemAnimator().setMoveDuration(300);
        recyclerView.getItemAnimator().setRemoveDuration(200);
        recyclerView.getItemAnimator().setAddDuration(300);
    }

    private void setUpRecyclerViewGuests() {

        recyclerViewGuests = (RecyclerView) view.findViewById(R.id.recyclerViewGuests);
        adapterGuests = new GuestAdapter(mContext, guests);
        recyclerViewGuests.setAdapter(adapterGuests);

        LinearLayoutManager mLinearLayoutManagerVertical = new LinearLayoutManager(mContext); // (Context context, int spanCount)
        mLinearLayoutManagerVertical.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerViewGuests.setLayoutManager(mLinearLayoutManagerVertical);

        recyclerViewGuests.getItemAnimator().setChangeDuration(400);
        recyclerViewGuests.getItemAnimator().setMoveDuration(300);
        recyclerViewGuests.getItemAnimator().setRemoveDuration(200);
        recyclerViewGuests.getItemAnimator().setAddDuration(300);
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
                    if(g.userID.equals(UserID))
                    {
                        Allreadyloggedin = true;
                        mGuestsRef.removeEventListener(this);
                    }
                }
                if(Allreadyloggedin == false)
                {
                    String thisUserName = sharedPreferencesData.getFacebookFullName(mContext);
                    String thisUserID = sharedPreferencesData.getFacebookUID(mContext);
                    mGuestsRef.child(thisUserID).setValue(new Guest(thisUserID,thisUserName,facebookPicUri));
                    Allreadyloggedin = true;
                    mGuestsRef.removeEventListener(this);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mGuestsRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s)
            {
                Guest g = dataSnapshot.getValue(Guest.class);
                adapterGuests.addGuest(g);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Guest g = dataSnapshot.getValue(Guest.class);
                adapterGuests.SetPoints(g);

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
}
