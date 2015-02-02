package com.patriciasays.wingman.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;

import com.patriciasays.wingman.CompetitionToolApp;

public class NetworkStatusReceiver extends BroadcastReceiver {

    public NetworkStatusReceiver() {
        super();
        CompetitionToolApp.getInstance().updateNetworkConnectivityStatus();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        CompetitionToolApp.getInstance().updateNetworkConnectivityStatus();
    }

    public static void registerReceiver(Context context, NetworkStatusReceiver receiver) {
        IntentFilter filter = new IntentFilter();
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        context.registerReceiver(receiver, filter);
    }
}
