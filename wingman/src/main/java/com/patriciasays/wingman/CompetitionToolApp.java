package com.patriciasays.wingman;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.patriciasays.wingman.lib.MicrophoneStatusReceiver;
import com.patriciasays.wingman.lib.NetworkStatusReceiver;

public class CompetitionToolApp extends Application {

    private static CompetitionToolApp sInstance;

    private boolean mIsNetworkConnected;
    private boolean mIsMicAvailable;

    @Override
    public void onCreate() {
        super.onCreate();

        sInstance = this;

        MicrophoneStatusReceiver.registerReceiver(this, new MicrophoneStatusReceiver());
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

    public boolean getMicConnectedStatus() {
        return mIsMicAvailable;
    }

    public void setMicConnectedStatus(boolean status) {
        mIsMicAvailable = status;
    }

}
