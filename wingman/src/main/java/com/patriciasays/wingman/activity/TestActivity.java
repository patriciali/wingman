package com.patriciasays.wingman.activity;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class TestActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        (new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(final Void... params) {
                JSONObject resultObject = new JSONObject();
                JSONObject solveTime = new JSONObject();
                try {
                    solveTime.put("millis", 10);
                    solveTime.put("decimals", 2);

                    resultObject.put("registrationId", "2CaHBnbSqkWaerRPs");
                    resultObject.put("solveTime", solveTime);
                    resultObject.put("solveIndex", 0);
                } catch (JSONException e) {
                    Log.d("fuck", e.toString());
                }

                int TIMEOUT_MILLISEC = 10000;  // = 10 seconds
                HttpParams httpParams = new BasicHttpParams();
                HttpConnectionParams.setConnectionTimeout(httpParams, TIMEOUT_MILLISEC);
                HttpConnectionParams.setSoTimeout(httpParams, TIMEOUT_MILLISEC);
                HttpClient client = new DefaultHttpClient(httpParams);

                HttpPut request = new HttpPut("http://staging.live.jflei.com/api/v0/competitions/USNationals2014/rounds/333/4/results?token=ZzK12RXY-Vl3mGFGsCn4ka8UI2EqQM2wwa01nCZizPR");
                StringEntity se = null;
                try {
                    se = new StringEntity(resultObject.toString());
                } catch (UnsupportedEncodingException e) {
                    Log.d("fuck", e.toString());
                }
                se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
                request.setEntity(se);
                try {
                    HttpResponse response = client.execute(request);
                } catch (IOException e) {
                    Log.d("fuck", e.toString());
                }

                return null;
            }
        }).execute();

    }

}
