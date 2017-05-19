package cpmusic.com.crowdplay.util;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import cpmusic.com.crowdplay.model.firebaseModel.Track;
import cpmusic.com.crowdplay.model.firebaseModel.Tracks;
import cpmusic.com.crowdplay.model.spotifyModel.Example;
import cpmusic.com.crowdplay.model.spotifyModel.Item;

/**
 * Created by rrask on 17-05-2017.
 */

public class APIConnector {

    RequestQueue queue;
    String webResponse;
    Example data;


    private Intent dataIntent;
    private LocalBroadcastManager localBroadcastManager;

    public APIConnector(Context c)
    {
        dataIntent = new Intent("SearchData");
        localBroadcastManager = LocalBroadcastManager.getInstance(c);
    }

    public void Search(String search, Context c)
    {
        String url = "https://api.spotify.com/v1/search?q=" + search + "&type=artist,track";

        sendRequest(url, c);
    }


    public void sendRequest(String url, Context c) {
        //send request using Volley
        if (queue == null) {
            queue = Volley.newRequestQueue(c);
        }

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        webResponse = response;
                        data = JSONParser.parseSearchWithJsonParser(webResponse);

                        SortData();


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });

        queue.add(stringRequest);

    }

    private void SortData()
    {
        ArrayList<Track> tracks = new ArrayList<>();

        for (Item i : data.tracks.items)
        {
            Track track = new Track();

            track.Title = i.name;
            track.Artist = i.artists.get(0).name;
            track.Album = i.album.name;
            track.ImageURL = i.album.images.get(0).url;
            track.URI = i.uri;
            track.Votes = 0;

            tracks.add(track);
        }

        dataIntent.putExtra("tracks", tracks);
        localBroadcastManager.sendBroadcast(dataIntent);
    }
}
