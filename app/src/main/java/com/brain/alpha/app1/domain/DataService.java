package com.brain.alpha.app1.domain;


import android.os.StrictMode;
import android.util.Log;


import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.brain.alpha.app1.MainActivity;

import com.fasterxml.jackson.databind.ObjectMapper;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DataService {
    public static List<TrackCategory> categoryList = new ArrayList<>();

     static {

//         getJson("http://172.20.10.5:8080/");
//         get2();
//         add("Create1C30Min", 1000L, "Create", "foobar");
//         add("Relax1C30Min", 2000L, "Relax", "foobar Relax1C30Min");
//         add("Relax1A10Min", 2500L, "Relax", "foobar Relax1A10Min x11");

         getRemoteCategoryList();
//         getRemoteTrackList(categoryList.get(0).links );

         System.out.print("<<***>> " + categoryList.get(0).getUriLinksTo("tracks"));

         System.out.println("DataService.static initializer: " + getRemoteTrackList(categoryList.get(0)));
//         getRemoteTrackList(categoryList.get(0).getUriLinksTo("tracks"));
     }



    public static void getRemoteCategoryList(){
        categoryList.clear();
        String baseUrl = "http://192.168.0.107:8080/";
        categoryList = (List<TrackCategory>)traverse(baseUrl, "categories", TrackCategory.class);
    }

    public static List<? extends HalResource> traverse(String uri, String hop, Class<? extends HalResource> targetClass){
        System.out.println(">>>>>>> uri: "+uri);

        List<HalResource> resultList= new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();
        JSONObject response = getJson(uri);
        List<String> links= new ArrayList<>();

        try {
            JSONArray rawList =  ((JSONObject)response.get("_links")).getJSONArray(hop);
            for (int i = 0; i < rawList.length(); i++) {
                links.add((String)rawList.getJSONObject(i).get("href"));
                System.out.println("-----------");
            }
            System.out.println("^^^^ _links: "+links);
            for(String itemUri : links){
                resultList.add(getHalResource(itemUri, targetClass));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return resultList;
    }

    public static HalResource getHalResource(String itemUri, Class<? extends HalResource> targetClass) {
        ObjectMapper mapper = new ObjectMapper();

                HalResource item = null;
                try {
                    JSONObject response = getJson(itemUri);
                    System.out.println("----------- "+response);
                    item = mapper.readValue(response.toString(), targetClass);
                    item.links = (JSONObject)(response.get("_links"));

                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
        return item;

    }

    public static Track getRemoteTrack(String uri){
        return (Track)getHalResource(uri, Track.class);
    }

    public static List<Track> getRemoteTrackList(TrackCategory category){
        List<String> items = category.getUriLinksTo("tracks");
        List<Track> tracks = new ArrayList<>();
        for(String uri :items){
            tracks.add((Track) getHalResource(uri, Track.class));
        }
        Log.i("tracks1" , tracks.toString());
        return tracks;
    }

    public static JSONObject getJson(String stringUrl) {
        System.out.println("++++++++ GET1 *****" );
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        HttpURLConnection urlConnection = null;

        StringBuilder result = new StringBuilder();

        try {
            URL url = new URL(stringUrl);
            urlConnection = (HttpURLConnection) url.openConnection();
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());

            BufferedReader reader = new BufferedReader(new InputStreamReader(in));

            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }

        }catch( Exception e) {
            e.printStackTrace();
        }
        finally {
            urlConnection.disconnect();
        }

        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(result.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        System.out.println("++++++++ " );
        System.out.println( jsonObject );
        System.out.println("++++++++ " );
        return jsonObject;


    }
}

/*
* Create1C30Min.mp3
Relax1C30Min.mp3
Sleep1C30Min.mp3
Create1B20Min.mp3
Relax1B20Min.mp3
Sleep1B20Min.mp3
SOS1A15Min.mp3
SOS1B15Min.mp3
SOS1C15Min.mp3
Create1A10Min.mp3
Relax1A10Min.mp3
Sleep1A10Min.mp3
* */
