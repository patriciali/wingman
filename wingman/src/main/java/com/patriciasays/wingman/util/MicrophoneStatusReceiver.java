package com.patriciasays.wingman.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class MicrophoneStatusReceiver extends BroadcastReceiver {

    private static final int INVALID_NO_EXTRA = -1;

    private static boolean mMicAvailable;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_HEADSET_PLUG)) {
            int headsetState = intent.getIntExtra("state", INVALID_NO_EXTRA);
            int micState = intent.getIntExtra("microphone", INVALID_NO_EXTRA);

            mMicAvailable = headsetState == 1 && micState == 1;
        }
    }

    public boolean isMicAvailable() {
        return mMicAvailable;
    }
}
