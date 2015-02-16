package com.patriciasays.wingman.activity.common;

import android.content.Intent;

import com.patriciasays.wingman.CompetitionToolApp;
import com.patriciasays.wingman.activity.util.NetworkSettingsActivity;

public abstract class NeedsNetworkActivity extends WingmanActivity {

    @Override
    protected void onStart() {
        super.onStart();

        if (!CompetitionToolApp.getInstance().getNetworkConnectivityStatus()) {
            startActivity(new Intent(this, NetworkSettingsActivity.class));
        }
    }

}
