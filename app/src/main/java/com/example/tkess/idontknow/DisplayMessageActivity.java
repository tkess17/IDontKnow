package com.example.tkess.idontknow;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_message);

        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);

        // Capture the layout's TextView and set the string as its text
        //TextView textView = findViewById(R.id.textView);
        //textView.setText(message);

        final TextView mTextView = (TextView) findViewById(R.id.textView);
// ...

// Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=40.4842,-88.9937&radius=1500&type=restaurant&key=AIzaSyCwByeJi83SFYSeLrd3CP5CJ_bvCGVaW5Y";

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
                            Random rand = new Random();
                            String randomRestaurant = restaurants.get(rand.nextInt(restaurants.size()));
                            mTextView.setText("Random restaurant result " + randomRestaurant);

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
