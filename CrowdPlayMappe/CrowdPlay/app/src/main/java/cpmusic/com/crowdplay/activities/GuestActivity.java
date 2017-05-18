package cpmusic.com.crowdplay.activities;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import cpmusic.com.crowdplay.R;
import cpmusic.com.crowdplay.adapters.SearchAdapter;
import cpmusic.com.crowdplay.model.firebaseModel.Track;
import cpmusic.com.crowdplay.model.firebaseModel.Tracks;
import cpmusic.com.crowdplay.model.spotifyModel.Example;
import cpmusic.com.crowdplay.model.spotifyModel.Item;
import cpmusic.com.crowdplay.util.APIConnector;
import cpmusic.com.crowdplay.util.NetworkChecker;

public class GuestActivity extends AppCompatActivity {

    EditText editSearch;
    FloatingActionButton fabSearch;
    ListView listView;

    NetworkChecker networkChecker;
    APIConnector apiConnector;

    SearchAdapter adapter;

    Tracks tracks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest);

        networkChecker = new NetworkChecker();
        apiConnector = new APIConnector(this);

        editSearch = (EditText)findViewById(R.id.editSearch);
        fabSearch = (FloatingActionButton)findViewById(R.id.fabSearch);
        listView = (ListView) findViewById(R.id.listView);

        LocalBroadcastManager.getInstance(this).registerReceiver(mReceiveFromService, new IntentFilter("SearchData"));

        fabSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Search();
            }
        });

        tracks = new Tracks();
        tracks.tracks = new ArrayList<>();

        adapter = new SearchAdapter(this, tracks.tracks);

        listView.setAdapter(adapter);
    }

    public void setData()
    {
        adapter.clear();
        adapter.addAll(tracks.tracks);
    }

    private BroadcastReceiver mReceiveFromService = new BroadcastReceiver()
    {
        @Override
        public void onReceive(Context context, Intent intent) {
            tracks = (Tracks) intent.getExtras().getSerializable("tracks");

            setData();
        }
    };

    public void Search()
    {
        if(networkChecker.getNetworkStatus(this))
        {
            apiConnector.Search(editSearch.getText().toString(), this);
        }
    }
}
