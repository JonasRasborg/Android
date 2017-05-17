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
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.squareup.picasso.Picasso;

import java.util.List;

import cpmusic.com.crowdplay.R;
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

    ArrayAdapter<String> adapter;

    Example data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest);

        networkChecker = new NetworkChecker();
        apiConnector = new APIConnector(this);

        editSearch = (EditText)findViewById(R.id.editSearch);
        fabSearch = (FloatingActionButton)findViewById(R.id.fabSearch);
        listView = (ListView)findViewById(R.id.listView);

        LocalBroadcastManager.getInstance(this).registerReceiver(mReceiveFromService, new IntentFilter("SearchData"));

        fabSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Search();
            }
        });

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1);

        listView.setAdapter(adapter);
    }

    private void setData()
    {
        adapter.clear();

        for (Item i : data.tracks.items)
        {
            adapter.add(i.artists.get(0).name + " - " + i.name);

            //Picasso.with(this).load(i.album.images.get(0).url).into(imageView);
        }
    }

    private BroadcastReceiver mReceiveFromService = new BroadcastReceiver()
    {
        @Override
        public void onReceive(Context context, Intent intent) {
            data = (Example) intent.getExtras().getSerializable("data");
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
