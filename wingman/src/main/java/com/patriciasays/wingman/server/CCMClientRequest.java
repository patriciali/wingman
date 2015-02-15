package com.patriciasays.wingman.server;

import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;

import java.io.UnsupportedEncodingException;

/**
 * StringRequest with data
 */
public class CCMClientRequest extends Request<String> {

    private static final String TAG = "CCMClientRequest";

    private String mBody;
    private Response.Listener<String> mResponseListener;

    public CCMClientRequest(String url,
                            String body,
                            Response.Listener<String> responseListener,
                            Response.ErrorListener errorListener) {
        super(Method.GET, url, errorListener);
        mBody = body;
        mResponseListener = responseListener;
    }

    @Override
    public byte[] getBody() {
        try {
            return mBody.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            Log.e(TAG, e.toString());
            return new byte[]{};
        }
    }

    @Override
    protected Response parseNetworkResponse(NetworkResponse response) {
        String parsed;
        try {
            parsed = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
        } catch (UnsupportedEncodingException e) {
            parsed = new String(response.data);
        }
        return Response.success(parsed, HttpHeaderParser.parseCacheHeaders(response));
    }

    @Override
    protected void deliverResponse(String response) {
        mResponseListener.onResponse(response);
    }
}
