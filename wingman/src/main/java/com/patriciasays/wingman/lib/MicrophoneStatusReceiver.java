package com.patriciasays.wingman.lib;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.patriciasays.wingman.CompetitionToolApp;

public class MicrophoneStatusReceiver extends BroadcastReceiver {

    private static final int INVALID_NO_EXTRA = -1;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_HEADSET_PLUG)) {
            int headsetState = intent.getIntExtra("state", INVALID_NO_EXTRA);
            int micState = intent.getIntExtra("microphone", INVALID_NO_EXTRA);

            CompetitionToolApp.getInstance().setMicConnectedStatus(
                    headsetState == 1 && micState == 1);
        }
    }

    public static void registerReceiver(Context context, MicrophoneStatusReceiver receiver) {
        IntentFilter filter = new IntentFilter(Intent.ACTION_HEADSET_PLUG);
        context.registerReceiver(receiver, filter);
    }
}
