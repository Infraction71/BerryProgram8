package com.example.berryprogram8;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    TextView displayTV;
    Button tidBTN,shipBTN;

    int seed = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Getting buttons and views from activity
        displayTV = findViewById(R.id.dispalyTV);
        tidBTN = findViewById(R.id.tidBTN);
        //Both OCLs do the same thing, one for each button
        tidBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread (new Runnable () {
                    @Override
                    public void run() {
                        try {
                            //Builds a URL to send to SWAPI, then calls methods to handle it
                            String urlStr = "https://swapi.co/api/planets/";
                            String params = seedBuilder(50);
                            URL url = new URL(urlStr+params);
                            String json = fetchFromURL(url);
                            Log.d("JSON Fetch", json); // Lets see what we got
                            processJSON(json);
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        }
                    }}).start();
            }
        });
        shipBTN = findViewById(R.id.shipBTN);
        shipBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread (new Runnable () {
                    @Override
                    public void run() {
                        try {

                            String urlStr = "https://swapi.co/api/starships/";
                            String params = seedBuilder(44);
                            URL url = new URL(urlStr+params);
                            String json = fetchFromURL(url);
                            Log.d("JSON Fetch", json); // Lets see what we got
                            processJSON(json,true);
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        }
                    }}).start();
            }
        });
    }

    private String fetchFromURL(URL url) {
        //Gets the requested JSON doc from SWAPi and returns it as a string
        StringBuilder strBuilder = new StringBuilder();
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
            String line;
            while((line = br.readLine()) != null)
                strBuilder.append(line);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d("Jason", strBuilder.toString());
        return strBuilder.toString();
    }

    private String seedBuilder(int bound){
        //Simple method to create the neccessary extension
        Random rnadom = new Random();
        seed = rnadom.nextInt(bound);
        return seed+"/";
    }
    //Overloaded method. One for starships. First one is for planets
    public void processJSON(final String json){
        this.runOnUiThread (new Runnable() {
            @Override
            public void run() {
                try{
                    //Turns String into a JSON object which we can then call getString to... get strings
                    JSONObject responsee = new JSONObject(json);
                    String rtrn = "Name: ";
                    rtrn +=responsee.getString("name");
                    rtrn +="\n Climate: " + responsee.getString("climate");
                    rtrn +="\n Population: " + responsee.getString("population");
                    displayTV.setText(""+rtrn);
                }
                catch (JSONException e){
                    Log.d("Jason", "Stuff's brooke\n" + e.getStackTrace());
                }
            } // end run
        });
    }
    public void processJSON(final String json, final boolean isShip){
        this.runOnUiThread (new Runnable() {
            @Override
            public void run() {
                if(isShip) {
                    try {
                        JSONObject responsee = new JSONObject(json);
                        String rtrn = "Name: ";
                        rtrn += responsee.getString("name");
                        rtrn += "\n Class: " + responsee.getString("starship_class");
                        rtrn += "\n Cost(Credits): " + responsee.getString("cost_in_credits");
                        displayTV.setText("" + rtrn);
                    } catch (JSONException e) {
                        Log.d("Jason", "Stuff's brooke\n" + e.getStackTrace());
                    }

                }
            } // end run
        });
    }

}