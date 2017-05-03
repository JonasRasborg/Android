package com.example.group02.weatheraarhusgroup02.Utilities;

import android.app.Service;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

/**
 * Created by Rune Rask on 03-05-2017.
 */

public class WebConnector {
    RequestQueue queue;

    //Følgende flyttes til CheckNetwork eller Service.
    public void checkNetwork(Service s){
        String status = NetworkChecker.getNetworkStatus(s);
    }

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


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        queue.add(stringRequest);

    }


    //attempt to decode the json response from weather server
    public void interpretWeatherJSON(String jsonResonse){

        // txtJsonResponse.setText(WeatherJsonParser.parseCityWeatherJsonWithGson(jsonResonse));
    }
}
