package cpmusic.com.crowdplay.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import cpmusic.com.crowdplay.R;
import cpmusic.com.crowdplay.adapters.SearchAdapter;
import cpmusic.com.crowdplay.model.firebaseModel.Track;
import cpmusic.com.crowdplay.util.APIConnector;
import cpmusic.com.crowdplay.util.NetworkChecker;

public class SearchActivity extends AppCompatActivity {

    EditText editSearch;
    FloatingActionButton fabSearch;

    NetworkChecker networkChecker;
    APIConnector apiConnector;

    SearchAdapter adapter;

    RecyclerView recyclerView;

    ArrayList<Track> tracks;

    FirebaseDatabase database;
    DatabaseReference mPartyRef;

    String partyID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest);

        Intent partyIntent = getIntent();
        partyID = partyIntent.getStringExtra("ID");

        networkChecker = new NetworkChecker();
        apiConnector = new APIConnector(this);

        editSearch = (EditText)findViewById(R.id.editSearch);
        fabSearch = (FloatingActionButton)findViewById(R.id.fabSearch);

        LocalBroadcastManager.getInstance(this).registerReceiver(mReceiveFromService, new IntentFilter("SearchData"));

        fabSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Search();
            }
        });

        tracks = new ArrayList<>();

        database = FirebaseDatabase.getInstance();
        mPartyRef = database.getReference().child(partyID);

        setUpRecyclerView();
    }


    private BroadcastReceiver mReceiveFromService = new BroadcastReceiver()
    {
        @Override
        public void onReceive(Context context, Intent intent) {
            tracks = (ArrayList<Track>) intent.getExtras().getSerializable("tracks");
            adapter.addTrack(tracks);
        }
    };

    public void Search()
    {
        if(networkChecker.getNetworkStatus(this))
        {
            apiConnector.Search(editSearch.getText().toString(), this);
        }
    }

    private void setUpRecyclerView() {

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        adapter = new SearchAdapter(this, tracks, mPartyRef);
        recyclerView.setAdapter(adapter);

        LinearLayoutManager mLinearLayoutManagerVertical = new LinearLayoutManager(this); // (Context context, int spanCount)
        mLinearLayoutManagerVertical.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(mLinearLayoutManagerVertical);

        recyclerView.getItemAnimator().setChangeDuration(400);
        recyclerView.getItemAnimator().setMoveDuration(300);
        recyclerView.getItemAnimator().setRemoveDuration(200);
        recyclerView.getItemAnimator().setAddDuration(300);
    }
}
