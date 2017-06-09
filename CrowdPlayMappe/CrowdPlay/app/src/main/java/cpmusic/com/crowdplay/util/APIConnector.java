package cpmusic.com.crowdplay.util;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cpmusic.com.crowdplay.model.firebaseModel.Track;
import cpmusic.com.crowdplay.model.spotifyModel.Auth;
import cpmusic.com.crowdplay.model.spotifyModel.Example;
import cpmusic.com.crowdplay.model.spotifyModel.Item;

/**
 * Created by rrask on 17-05-2017.
 */

public class APIConnector {

    RequestQueue queue;

    public String token;



    private Intent dataIntent;
    private LocalBroadcastManager localBroadcastManager;

    private Intent drakeIntent;

    public APIConnector(Context c)
    {
        dataIntent = new Intent("SearchData");
        localBroadcastManager = LocalBroadcastManager.getInstance(c);

        drakeIntent = new Intent("Drake");

        getAuth(c);
    }

    public void Search(String search, Context c)
    {
        getAuth(c);

        String[] searchWords = search.split(" ");

        String searchString = "";

        for (int i = 0; i<searchWords.length; i++)
        {
            searchString += "+" + searchWords[i];
        }

        String url = "https://api.spotify.com/v1/search?q=" + searchString + "&type=artist,track";

        sendRequest(url, c);
    }

    private void setToken(String _token)
    {
        token = _token;
    }

    public void getAuth(Context c)
    {
        final String clientID = "NTlkNWE2NzU4YjlkNGE3OWE2NzkzMjNhNjVhZDhhMWI6MDU4MDJhZWZiMTkxNDc4ODg3MTAxNjFhNzkzNWQ5NTg=";

        String url = "https://accounts.spotify.com/api/token?grant_type=client_credentials";

        if (queue == null) {
            queue = Volley.newRequestQueue(c);
        }

        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        String webResponse = response;

                        Auth data = JSONParser.parseAuthWithJsonParser(webResponse);

                        String token = data.accessToken;

                        setToken(token);

                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        int a = 0;
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("Authorization", "Basic " + clientID);
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };

        queue.add(postRequest);

    }

public void sendRequest(String url, Context c)
{
    if (queue == null) {
        queue = Volley.newRequestQueue(c);
    }

    StringRequest postRequest = new StringRequest(Request.Method.GET, url,
            new Response.Listener<String>()
            {
                @Override
                public void onResponse(String response) {
                    String webResponse = response;
                    Example data = JSONParser.parseSearchWithJsonParser(webResponse);

                    SortData(data);

                    // response
                }
            },
            new Response.ErrorListener()
            {
                @Override
                public void onErrorResponse(VolleyError error) {
                    // TODO Auto-generated method stub

                    ArrayList<Track> tracks = new ArrayList<>();
                    dataIntent.putExtra("tracks", tracks);
                    localBroadcastManager.sendBroadcast(dataIntent);
                }
            }
    ) {
    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String>  params = new HashMap<String, String>();
        params.put("Authorization", "Bearer " + token);
        params.put("Accept", "application/json");
        return params;
    }
};

    queue.add(postRequest);
}


    private void SortData(Example data)
    {
        ArrayList<Track> tracks = new ArrayList<>();

        for (Item i : data.tracks.items)
        {
            Track track = new Track();

            track.Title = i.name;
            track.Artist = i.artists.get(0).name;
            track.Album = i.album.name;
            if (i.album.images.size() != 0) {track.ImageURL = i.album.images.get(0).url;}
            track.URI = i.uri;
            track.Voters = new HashMap<>();

            tracks.add(track);
        }

        dataIntent.putExtra("tracks", tracks);
        localBroadcastManager.sendBroadcast(dataIntent);
    }



/*    public void sendRequestForDrake(Context c) {

        //send request using Volley
        if (queue == null) {
            queue = Volley.newRequestQueue(c);
        }

        String url = "https://api.spotify.com/v1/search?q=" + "Drake" + "&type=artist,track";

        StringRequest stringRequestDrake = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String webResponse = response;
                        Example data = JSONParser.parseSearchWithJsonParser(webResponse);

                        if (data.tracks.items != null)
                        {
                            drakeIntent.putExtra("DrakeData", data.tracks);
                            localBroadcastManager.sendBroadcast(drakeIntent);
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });

        queue.add(stringRequestDrake);
    }*/

    public void sendRequestForDrake(Context c)
    {
        if (queue == null) {
            queue = Volley.newRequestQueue(c);
        }

        String url = "https://api.spotify.com/v1/search?q=" + "Drake" + "&type=artist,track";

        StringRequest postRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {

                        String webResponse = response;
                        Example data = JSONParser.parseSearchWithJsonParser(webResponse);

                        if (data.tracks.items != null)
                        {
                            drakeIntent.putExtra("DrakeData", data.tracks);
                            localBroadcastManager.sendBroadcast(drakeIntent);
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub

                        ArrayList<Track> tracks = new ArrayList<>();
                        dataIntent.putExtra("tracks", tracks);
                        localBroadcastManager.sendBroadcast(dataIntent);
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("Authorization", "Bearer " + token);
                params.put("Accept", "application/json");
                return params;
            }
        };

        queue.add(postRequest);
    }

}
