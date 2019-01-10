package com.example.tkess.idontknow;

import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DisplayMessageActivity extends AppCompatActivity {

    public double latitude;
    public double longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_message);

        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        String radius = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
        if(radius.length() == 0) {
            radius = "1";
        }
        int radiusMiles = Integer.parseInt(radius);
        int radiusMeters = radiusMiles * 1609;
        System.out.println("final radius " + radiusMeters);



        //Coordinate stuff
//        LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
//        Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
//        double longitude = location.getLongitude();
//        double latitude = location.getLatitude();
//        System.out.println("coords" + longitude);

        LocationManager locationManager = (LocationManager)  this.getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String bestProvider = String.valueOf(locationManager.getBestProvider(criteria, true)).toString();

        //You can still do this if you like, you might get lucky:
        Location location = locationManager.getLastKnownLocation(bestProvider);
        if (location != null) {
            Log.e("TAG", "GPS is on");
            latitude = location.getLatitude();
            longitude = location.getLongitude();
            Toast.makeText(DisplayMessageActivity.this, "latitude:" + latitude + " longitude:" + longitude, Toast.LENGTH_SHORT).show();
            //searchNearestPlace(voice2text);
        }

        final TextView mTextView = (TextView) findViewById(R.id.textView);
// ...

// Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="https://maps.googleapis.com/maps/api/place/nearbysearch/json?location="+latitude+"+,"+longitude+"&radius="+radiusMeters+"&type=restaurant&key=AIzaSyCwByeJi83SFYSeLrd3CP5CJ_bvCGVaW5Y";

// Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject placeJSONResponse = new JSONObject(response);
                            JSONArray results = placeJSONResponse.getJSONArray("results");
                            System.out.println("results here : " + results);
                            List<String> restaurants = new ArrayList<>();
                            for(int i = 0; i < results.length(); i++) {
                                JSONObject single = results.getJSONObject(i);
                                restaurants.add(single.getString("name"));
                            }
                            System.out.println(restaurants.toString());
                            System.out.println(restaurants.size());
                            Random rand = new Random();
                            String randomRestaurant = restaurants.get(rand.nextInt(restaurants.size()));
                            mTextView.setText(randomRestaurant);

                        } catch (JSONException e) {
                            System.out.println("JSONException: " + e);
                            mTextView.setText("wtf");
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("error here: " + error);
                mTextView.setText("That didn't work!");
            }
        });

// Add the request to the RequestQueue.
        queue.add(stringRequest);
    }
}
