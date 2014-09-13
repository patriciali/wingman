package com.patriciasays.wingman.activity;

import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;

import com.jflei.fskube.FSKubeWrapper;
import com.patriciasays.wingman.R;

public class DisplayActivity extends MicrophoneListenerActivity {

    private static final int REFRESH_DISPLAY_FPS = 50;
    private static final int REFRESH_DISPLAY_INTERVAL_MILLIS = 1000/REFRESH_DISPLAY_FPS;

    private Handler mHandler;
    private Runnable mUpdateDisplayRunnable;

    private TextView mMicLevelView; // TODO patricia make this into a bar thingie
    private TextView mDisplayView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_activity);

        mMicLevelView = (TextView) findViewById(R.id.microphone_level_textview);
        mDisplayView = (TextView) findViewById(R.id.display_textview);

        mHandler = new Handler();
        mUpdateDisplayRunnable = new Runnable() {
            @Override
            public void run() {
                int millis = FSKubeWrapper.getTimeMillis();
                mDisplayView.setText("" + millis);

                mHandler.postDelayed(mUpdateDisplayRunnable, REFRESH_DISPLAY_INTERVAL_MILLIS);
            }
        };

        FSKubeWrapper.initialize(MicrophoneListenerActivity.SAMPLE_RATE);
        mHandler.post(mUpdateDisplayRunnable);
    }

    @Override
    protected void handleSamples(double[] normalizedSamples) {
        for(double sample : normalizedSamples) {
            if (FSKubeWrapper.addSample(sample)) {
                Log.d("shit", "" +FSKubeWrapper.getTimeMillis());
            }

        }
    }

}
