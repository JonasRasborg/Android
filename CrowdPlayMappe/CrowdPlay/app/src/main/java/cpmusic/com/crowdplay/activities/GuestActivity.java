package cpmusic.com.crowdplay.activities;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import cpmusic.com.crowdplay.Fragments.PlayListFragment;
import cpmusic.com.crowdplay.Fragments.SearchFragment;
import cpmusic.com.crowdplay.R;
import cpmusic.com.crowdplay.adapters.FragmentAdapter;
import cpmusic.com.crowdplay.adapters.GuestAdapter;
import cpmusic.com.crowdplay.model.firebaseModel.Guest;
import cpmusic.com.crowdplay.model.firebaseModel.Track;
import cpmusic.com.crowdplay.util.SharedPreferencesConnector;

public class GuestActivity extends AppCompatActivity {

    private ViewPager mViewPager;
    String partyID;

    ImageView imgAlbum;
    TextView tvTitle;
    TextView tvArtist;

    PlayListFragment playListFragment;
    SearchFragment searchFragment;

    GuestAdapter adapterGuests;
    RecyclerView recyclerViewGuests;

    FirebaseDatabase database;
    DatabaseReference mGuestsRef;
    ArrayList<Guest> guests;

    SharedPreferencesConnector sharedPreferencesConnector;
    String thisUserID;
    String thisUserName;
    String facebookPicUri;
    boolean Allreadyloggedin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        imgAlbum = (ImageView)findViewById(R.id.imgAlbum);
        tvTitle = (TextView)findViewById(R.id.tvTitle);
        tvArtist = (TextView)findViewById(R.id.tvArtist);

        guests = new ArrayList<>();

        sharedPreferencesConnector = new SharedPreferencesConnector();
        thisUserID = sharedPreferencesConnector.getFacebookUID(this);
        thisUserName = sharedPreferencesConnector.getFacebookFullName(this);
        facebookPicUri = sharedPreferencesConnector.getFacebookProfilepicUri(this);

        Intent partyIntent = getIntent();
        partyID = partyIntent.getStringExtra("PartyKey");

        mViewPager =(ViewPager) findViewById(R.id.container);
        setupViewPager(mViewPager);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        database = FirebaseDatabase.getInstance();
        setUpRecyclerViewGuests();
        addGuestToParty();
    }


    private void setupViewPager(ViewPager viewPager){
        FragmentAdapter adapter = new FragmentAdapter(getSupportFragmentManager());

        playListFragment = new PlayListFragment();
        searchFragment = new SearchFragment();

        Bundle bundle = new Bundle();
        bundle.putString("PartyKey", partyID);

        playListFragment.setArguments(bundle);
        searchFragment.setArguments(bundle);

        adapter.addFragment(playListFragment,getString(R.string.PlaylistTab));
        adapter.addFragment(searchFragment, getString(R.string.SearchTab));
        viewPager.setAdapter(adapter);
    }

    public void dispatchInformations(Track track){
        tvTitle.setText(track.Title);
        tvArtist.setText(track.Artist);
        Picasso.with(this).load(track.ImageURL).into(imgAlbum);
    }

    private void setUpRecyclerViewGuests() {

        recyclerViewGuests = (RecyclerView) findViewById(R.id.recyclerViewGuests);
        adapterGuests = new GuestAdapter(this, guests);
        recyclerViewGuests.setAdapter(adapterGuests);

        LinearLayoutManager mLinearLayoutManagerVertical = new LinearLayoutManager(this); // (Context context, int spanCount)
        mLinearLayoutManagerVertical.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerViewGuests.setLayoutManager(mLinearLayoutManagerVertical);

        recyclerViewGuests.getItemAnimator().setChangeDuration(400);
        recyclerViewGuests.getItemAnimator().setMoveDuration(300);
        recyclerViewGuests.getItemAnimator().setRemoveDuration(200);
        recyclerViewGuests.getItemAnimator().setAddDuration(300);
    }

    private void addGuestToParty()
    {
        mGuestsRef = database.getReference().child(partyID).child("Guests");

        mGuestsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot i:dataSnapshot.getChildren())
                {
                    Guest g = dataSnapshot.child(i.getKey()).getValue(Guest.class);
                    if(g.userID.equals(thisUserID))
                    {
                        Allreadyloggedin = true;
                        mGuestsRef.removeEventListener(this);
                    }
                }
                if(Allreadyloggedin == false)
                {
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

