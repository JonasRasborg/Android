package cpmusic.com.crowdplay.util;

import android.content.Context;
import android.support.v4.content.ContextCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import cpmusic.com.crowdplay.model.spotifyModel.Example;

/**
 * Created by rrask on 17-05-2017.
 */

public class APIConnector {

    RequestQueue queue;
    String webResponse;
    Example data;

    public void Search(String artist, String title, Context c)
    {
        String url = "https://api.spotify.com/v1/search?q=" + artist + "+" + title + "&type=artist,track";

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
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });

        queue.add(stringRequest);

    }
}
