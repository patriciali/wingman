package com.patriciasays.wingman.server;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.patriciasays.wingman.data.Average;
import com.patriciasays.wingman.data.Competition;
import com.patriciasays.wingman.data.Participant;
import com.patriciasays.wingman.data.Result;
import com.patriciasays.wingman.data.ResultWrapper;
import com.patriciasays.wingman.data.Round;
import com.patriciasays.wingman.util.Constants;

import java.lang.reflect.Type;
import java.util.List;

public class CCMClientApi {

    private static final String TAG = "CCMClientApi";

    private static CCMClientApi sInstance;

    private Context mContext;
    private SharedPreferences mSharedPreferences;
    private RequestQueue mRequestQueue;
    private Response.ErrorListener mErrorListener;

    public interface Listener<T> {
        public void onResponse(T response);
    }

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

    public Request<String> competitionsList(final Listener<List<Competition>> listener) {
        String url = getBaseUrl() + Constants.COMPETITIONS_URL_SUFFIX;
        Response.Listener<String> wrapper = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Type listType = new TypeToken<List<Competition>>(){}.getType();
                listener.onResponse((List<Competition>) new Gson().fromJson(response, listType));
            }
        };
        StringRequest request = new StringRequest(url, wrapper, mErrorListener);
        return enqueueRequest(request);
    }

    public Request<String> rounds(final Listener<List<Round>> listener) {
        String url = String.format(getBaseUrl() + Constants.ROUNDS_URL_SUFFIX, getCompetitionId());
        Response.Listener<String> wrapper = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Type listType = new TypeToken<List<Round>>(){}.getType();
                listener.onResponse((List<Round>) new Gson().fromJson(response, listType));
            }
        };
        StringRequest request = new StringRequest(url, wrapper, mErrorListener);
        return enqueueRequest(request);
    }

    public Request<String> participants(final Listener<List<Participant>> listener) {
        String url =
                String.format(getBaseUrl() + Constants.PARTICIPANTS_URL_SUFFIX, getCompetitionId());
        Response.Listener<String> wrapper = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Type listType = new TypeToken<List<Participant>>(){}.getType();
                listener.onResponse((List<Participant>) new Gson().fromJson(response, listType));
            }
        };
        StringRequest request = new StringRequest(url, wrapper, mErrorListener);
        return enqueueRequest(request);
    }

    public Request<String> averageInProgress(
            String eventCode, int nthRound, String regId, final Listener<Average> listener) {
        String url = String.format(getBaseUrl() + Constants.AVERAGE_IN_PROGRESS_URL_SUFFIX,
                getCompetitionId(), eventCode, nthRound, regId);
        Response.Listener<String> wrapper = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Type listType = new TypeToken<List<Average>>(){}.getType();
                List<Average> averages = new Gson().fromJson(response, listType);
                listener.onResponse(averages.get(0));
            }
        };
        StringRequest request = new StringRequest(url, wrapper, mErrorListener);
        return enqueueRequest(request);
    }

    public Request<String> submitTime(ResultWrapper resultWrapper,
                                      final Listener<ResultWrapper> listener) {
        String url = String.format(getBaseUrl() + Constants.UPLOAD_TIME_URL_SUFFIX,
                getCompetitionId(), resultWrapper.eventCode, resultWrapper.nthRound,
                mSharedPreferences.getString(
                        Constants.AUTH_TOKEN_PREFERENCE_KEY, Constants.AUTH_TOKEN_STOPSHIP));
        Response.Listener<Result> wrapper = new Response.Listener<Result>() {
            @Override
            public void onResponse(Result response) {
                // TODO we don't look at response but we should (in case of failure)
            }
        };

        String body = new Gson().toJson(resultWrapper.result);

        JsonRequest<Result> request = new JsonRequest<Result>(Request.Method.PUT, url,
                body, wrapper, mErrorListener) {
            @Override
            protected Response<Result> parseNetworkResponse(NetworkResponse response) {
                return null;
            }
        };
        return enqueueRequest(request);
    }

    private Request<String> enqueueRequest(Request request) {
        return mRequestQueue.add(request);
    }

    private String getBaseUrl() {
        return mSharedPreferences.getString(Constants.URL_PREFERENCE_KEY,
                Constants.DEFAULT_SERVER_URL);
    }

    private String getCompetitionId() {
        return mSharedPreferences.getString(Constants.COMPETITION_ID_PREFERENCE_KEY, ""); // TODO
    }

}
