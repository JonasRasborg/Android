package hartogsohn.crowdplayapitest;

import android.app.Activity;
import android.app.Application;
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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import hartogsohn.crowdplayapitest.Model.Example;
import hartogsohn.crowdplayapitest.Model.Item;
import hartogsohn.crowdplayapitest.Model.Tracks;

public class MainActivity extends AppCompatActivity {

    EditText editArtist;
    EditText editTitle;

    FloatingActionButton fabSearch;

    ListView listView;

    NetWorkChecker netWorkChecker;

    Example data;

    RequestQueue queue;
    String webResponse;

    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editArtist = (EditText)findViewById(R.id.editArtist);
        editTitle = (EditText)findViewById(R.id.editTitle);
        fabSearch = (FloatingActionButton)findViewById(R.id.fabSearch);

        fabSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Search();
            }
        });

        listView = (ListView) findViewById(R.id.listView);

        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1);

        listView.setAdapter(adapter);

    }


    public void Search()
    {
        if(netWorkChecker.getNetworkStatus(this))
        {
            Search(editArtist.getText().toString(), editTitle.getText().toString());
        }
    }



    public void Search(String artist, String title)
    {
        String url = "https://api.spotify.com/v1/search?q=" + artist + "+" + title + "&type=artist,track";

        sendRequest(url);
    }


    public void sendRequest(String url) {
        //send request using Volley
        if (queue == null) {
            queue = Volley.newRequestQueue(this);
        }

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        webResponse = response;
                        data = JSONParser.parseSearchWithJsonParser(webResponse);

                        for (Item i : data.tracks.items)
                        {
                            adapter.add(i.artists.get(0).name + " - " + i.name);
                        }

                        //adapter.add(data.tracks.items.get(0).artists.get(0).name + data.tracks.items.get(0).name);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });

        queue.add(stringRequest);

    }

}
