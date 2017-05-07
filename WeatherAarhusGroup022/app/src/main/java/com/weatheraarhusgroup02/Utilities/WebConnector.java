package com.weatheraarhusgroup02.Utilities;

import android.app.Service;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.weatheraarhusgroup02.Model.WeatherInfo;
import com.weatheraarhusgroup02.Utilities.FireBaseConnector;

/**
 * Created by Rune Rask on 03-05-2017.
 */

public class WebConnector {
    RequestQueue queue;
    String webResponse;
    WeatherInfo weatherUpdate;
    DatabaseReference db = FirebaseDatabase.getInstance().getReference();

    FireBaseConnector fireBaseConnector = new FireBaseConnector(db);




    public void sendRequest(Service s){
        //send request using Volley
        if(queue==null){
            queue = Volley.newRequestQueue(s);
        }
        String url = Globals.WEATHER_API_CALL;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        webResponse = response;
                        weatherUpdate = JsonParser.parseCityWeatherJsonWithGson(webResponse);
                        fireBaseConnector.putData(weatherUpdate);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });

        queue.add(stringRequest);

    }
    public void sendRequestForLatest(Service s){
        //send request using Volley
        if(queue==null){
            queue = Volley.newRequestQueue(s);
        }
        String url = Globals.WEATHER_API_CALL;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        webResponse = response;
                        weatherUpdate = JsonParser.parseCityWeatherJsonWithGson(webResponse);
                        fireBaseConnector.putData(weatherUpdate);
                        }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });

        queue.add(stringRequest);

    }

}
