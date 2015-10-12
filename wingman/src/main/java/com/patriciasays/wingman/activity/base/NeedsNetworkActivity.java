package com.patriciasays.wingman.activity.base;

import android.content.Intent;

import com.patriciasays.wingman.CompetitionToolApp;

public abstract class NeedsNetworkActivity extends WingmanActivity {

    @Override
    protected void onStart() {
        super.onStart();

        if (!CompetitionToolApp.getInstance().getNetworkConnectivityStatus()) {
            startActivity(new Intent(this, NetworkSettingsActivity.class));
        }
    }

}
