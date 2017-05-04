package com.example.smap.weatherassignment.Utils;

import android.app.Activity;
import android.app.Service;
import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;


public class WebConnector {
    RequestQueue queue;
    String thisResponse;



    public String sendRequest(Activity activity){
        //send request using Volley
        if(queue==null){
            queue = Volley.newRequestQueue(activity);
        }
        String url = Globals.WEATHER_API_CALL;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        thisResponse = response;
                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        queue.add(stringRequest);

        return thisResponse;
    }

    public String sendRequest(Service service){
        //send request using Volley
        if(queue==null){
            queue = Volley.newRequestQueue(service);
        }
        String url = Globals.WEATHER_API_CALL;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        thisResponse = response;
                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        queue.add(stringRequest);

        return thisResponse;
    }

}
