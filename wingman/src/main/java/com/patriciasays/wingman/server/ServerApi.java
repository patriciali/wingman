package com.patriciasays.wingman.server;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;

import com.patriciasays.wingman.data.ServerConstants;
import com.patriciasays.wingman.util.Constants;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Be sure to use AsyncTask to call anything in here
 */
public class ServerApi {

    private static final String TAG = "ServerApi";

    public static List<String> getCompetitionsList(Context context) {
        String response = getJsonString(context, ServerConstants.COMPETITIONS_LIST_URL_SUFFIX);

        if (TextUtils.isEmpty(response)) {
            return new ArrayList<String>();
        }

        try {
            List<String> comps = new ArrayList<String>();
            JSONArray array = new JSONArray(response);
            for (int i = 0; i < array.length(); i += 1) {
                JSONObject competitionObject = (JSONObject) array.get(i);
                comps.add(competitionObject.getString(ServerConstants.KEY_COMPETITION_NAME));
            }
            return comps;
        } catch (JSONException e) {
            Log.e(TAG, "JSON response was not well-formed");
            e.printStackTrace();
            return new ArrayList<String>();
        }
    }

    private static String getJsonString(Context context, String route) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        String domainName = sharedPref.getString(Constants.DOMAIN_NAME_PREFERENCE_KEY, "");
        // TODO pzl how to use this?
        String portNum = sharedPref.getString(Constants.PORT_NUMBER_PREFERENCE_KEY, "");

        DefaultHttpClient client = new DefaultHttpClient(new BasicHttpParams());
        HttpGet request = new HttpGet("http://" + domainName + route);
        request.setHeader("Content-type", "application/json");

        InputStream inputStream = null;
        try {
            HttpResponse response = client.execute(request);
            HttpEntity entity = response.getEntity();

            inputStream = entity.getContent();
            // json is UTF-8 by default
            BufferedReader reader =
                    new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8);
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }

            return sb.toString();
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            return null;
        } finally {
            try {
                if(inputStream != null) {
                    inputStream.close();
                }
            } catch(Exception e) {
                Log.e(TAG, e.toString());
            }
        }
    }

}
