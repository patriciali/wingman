package com.patriciasays.wingman.server;

import android.util.Log;

import com.android.volley.Response;
import com.patriciasays.wingman.util.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ResponseWrapper {

    private static final String TAG = "ResponseWrapper";

    public interface Listener<T> {
        public void onResponse(T response);
    }

    public static Response.Listener<JSONArray> getCompetitionsListWrapper(
            final Listener<List<String>> listener) {
        return new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    List<String> comps = new ArrayList<String>();
                    for (int i = 0; i < response.length(); i += 1) {
                        JSONObject competitionObject = (JSONObject) response.get(i);
                        comps.add(competitionObject.getString(Constants.KEY_COMPETITION_NAME));
                    }
                    listener.onResponse(comps);
                } catch (JSONException e) {
                    Log.e(TAG, "JSON response was not well-formed");
                    listener.onResponse(new ArrayList<String>());
                }
            }
        };
    }

}
