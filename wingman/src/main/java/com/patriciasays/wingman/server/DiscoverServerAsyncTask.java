package com.patriciasays.wingman.server;

import android.content.Context;
import android.net.nsd.NsdManager;
import android.net.nsd.NsdServiceInfo;
import android.os.AsyncTask;
import android.util.Log;

import com.patriciasays.wingman.R;

public class DiscoverServerAsyncTask extends AsyncTask {

    private static final String TAG = "DiscoverServerAsyncTask";

    private NsdManager mNsdManager;
    private NsdManager.ResolveListener mResolveListener;
    private NsdManager.DiscoveryListener mDiscoveryListener;

    public DiscoverServerAsyncTask(final Context context) {
        mNsdManager = (NsdManager) context.getSystemService(Context.NSD_SERVICE);

        mResolveListener = new NsdManager.ResolveListener() {
            @Override
            public void onResolveFailed(NsdServiceInfo nsdServiceInfo, int i) {
                Log.d(TAG, "resolve failed");
            }

            @Override
            public void onServiceResolved(NsdServiceInfo nsdServiceInfo) {
                Log.d(TAG, "service resolved");
                Log.d(TAG, "ip: " + nsdServiceInfo.getHost());
                Log.d(TAG, "port: " + nsdServiceInfo.getPort());
            }
        };

        mDiscoveryListener = new NsdManager.DiscoveryListener() {
            @Override
            public void onDiscoveryStarted(String regType) {
                Log.d(TAG, "Service discovery started");
            }

            @Override
            public void onServiceFound(NsdServiceInfo service) {
                Log.d(TAG, "Service discovery success, " + service);
                Log.d(TAG, "name: " + service.getServiceName());
                Log.d(TAG, "type: " + service.getServiceType());

                String serverName = context.getResources().getString(R.string.server_name);
                if (service.getServiceName().contains(serverName)) {
                    Log.d(TAG, "found " + serverName + "; trying to resolve");

                    mNsdManager.resolveService(service, mResolveListener);

                }
            }

            @Override
            public void onServiceLost(NsdServiceInfo service) {
                Log.e(TAG, "service lost; " + service);
            }

            @Override
            public void onDiscoveryStopped(String serviceType) {
                Log.i(TAG, "Discovery stopped: " + serviceType);
            }

            @Override
            public void onStartDiscoveryFailed(String serviceType, int errorCode) {
                Log.e(TAG, "Discovery failed: Error code:" + errorCode);
                mNsdManager.stopServiceDiscovery(this);
            }

            @Override
            public void onStopDiscoveryFailed(String serviceType, int errorCode) {
                Log.e(TAG, "Discovery failed: Error code: " + errorCode);
                mNsdManager.stopServiceDiscovery(this);
            }
        };
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        mNsdManager.discoverServices("_http._tcp", NsdManager.PROTOCOL_DNS_SD, mDiscoveryListener);
        return null;
    }

}
