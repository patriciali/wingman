package com.patriciasays.wingman.server;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;
import com.patriciasays.wingman.util.Constants;

import org.json.JSONArray;

public class CCMClientApi {

    private static final String TAG = "CCMClientApi";

    private static CCMClientApi sInstance;

    private Context mContext;
    private SharedPreferences mSharedPreferences;
    private RequestQueue mRequestQueue;
    private Response.ErrorListener mErrorListener;

    private CCMClientApi(Context context) {
        mContext = context;
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        mRequestQueue = Volley.newRequestQueue(context); //TODO default?
        mErrorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, error.toString());
            }
        };
    }

    public static synchronized CCMClientApi getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new CCMClientApi(context.getApplicationContext());
        }
        return sInstance;
    }

    public Request<JSONArray> competitionsList(Response.Listener<JSONArray> listener) {
        String url = getUrl() + Constants.COMPETITIONS_LIST_URL_SUFFIX;
        JsonArrayRequest request = new JsonArrayRequest(url, listener, mErrorListener);
        return mRequestQueue.add(request);
    }

    private String getUrl() {
        return mSharedPreferences.getString(Constants.DOMAIN_NAME_PREFERENCE_KEY,
                Constants.DEFAULT_SERVER_URL);
    }

}
