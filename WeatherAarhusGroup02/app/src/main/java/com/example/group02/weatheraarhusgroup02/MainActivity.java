package com.example.group02.weatheraarhusgroup02;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.example.group02.weatheraarhusgroup02.Utilities.Globals;
import com.example.group02.weatheraarhusgroup02.Utilities.NetworkChecker;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpStack;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;


public class MainActivity extends AppCompatActivity {

    RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    private void checkNetwork(){
        String status = NetworkChecker.getNetworkStatus(this);
        Toast.makeText(this, status, Toast.LENGTH_SHORT).show();
    }

    private void sendRequest(){
        //send request using Volley
        if(queue==null){
            queue = Volley.newRequestQueue(this);
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
