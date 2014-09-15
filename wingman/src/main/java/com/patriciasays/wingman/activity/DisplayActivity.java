package com.patriciasays.wingman.activity;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import com.jflei.fskube.FSKubeWrapper;
import com.patriciasays.wingman.R;
import com.patriciasays.wingman.util.MicrophoneStatusReceiver;

public class DisplayActivity extends MicrophoneListenerActivity {

    private static final int REFRESH_DISPLAY_FPS = 50;
    private static final int REFRESH_DISPLAY_INTERVAL_MILLIS = 1000/REFRESH_DISPLAY_FPS;

    private MicrophoneStatusReceiver mMicStatusReceiver;
    private Handler mHandler;
    private Runnable mUpdateDisplayRunnable;

    private double mMicLevel;

    private TextView mMicLevelView; // TODO patricia make this into a bar thingie
    private TextView mMicStatusView;
    private TextView mDisplayView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_activity);

        mMicLevelView = (TextView) findViewById(R.id.microphone_level_textview);
        mMicStatusView = (TextView) findViewById(R.id.microphone_status_textview);
        mDisplayView = (TextView) findViewById(R.id.display_textview);

        mMicStatusReceiver = new MicrophoneStatusReceiver();
        IntentFilter receiverFilter = new IntentFilter(Intent.ACTION_HEADSET_PLUG);
        registerReceiver(mMicStatusReceiver, receiverFilter);

        mHandler = new Handler();
        mUpdateDisplayRunnable = new Runnable() {
            @Override
            public void run() {
                int millis = FSKubeWrapper.getTimeMillis();
                mDisplayView.setText("" + millis);
                mMicLevelView.setText("" + mMicLevel);
                mMicStatusView.setText("" + mMicStatusReceiver.isMicAvailable());

                mHandler.postDelayed(mUpdateDisplayRunnable, REFRESH_DISPLAY_INTERVAL_MILLIS);
            }
        };

        FSKubeWrapper.initialize(MicrophoneListenerActivity.SAMPLE_RATE);
        mHandler.post(mUpdateDisplayRunnable);
    }

    @Override
    protected void handleSamples(double[] normalizedSamples) {
        double amplitude = Double.MIN_VALUE;
        for(double sample : normalizedSamples) {
            if (FSKubeWrapper.addSample(sample)) {
                // this fires when FSKube recognizes a valid time
            }
            amplitude = Math.max(amplitude, sample);
        }
        mMicLevel = amplitude;
    }

}