package com.patriciasays.wingman;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.patriciasays.wingman.util.NetworkStatusReceiver;

public class CompetitionToolApp extends Application {

    private static CompetitionToolApp sInstance;

    private boolean mIsNetworkConnected;

    @Override
    public void onCreate() {
        super.onCreate();

        sInstance = this;

        NetworkStatusReceiver.registerReceiver(this, new NetworkStatusReceiver());
    }

    public static CompetitionToolApp getInstance() {
        return sInstance;
    }

    public boolean getNetworkConnectivityStatus() {
        return mIsNetworkConnected;
    }

    public void updateNetworkConnectivityStatus() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            mIsNetworkConnected = true;
        } else {
            mIsNetworkConnected = false;
        }
    }

}
