package com.patriciasays.wingman.activity.util;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;

import com.patriciasays.wingman.R;

public class NetworkSettingsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.need_network_activity);
    }

    public void wifiSettings(View view) {
        startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
        finish();
    }

}