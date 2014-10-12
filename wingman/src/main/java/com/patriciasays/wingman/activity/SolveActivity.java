package com.patriciasays.wingman.activity;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.jflei.fskube.FSKubeWrapper;
import com.patriciasays.wingman.R;
import com.patriciasays.wingman.util.MicrophoneStatusReceiver;
import com.patriciasays.wingman.util.Stopwatch;
import com.patriciasays.wingman.util.StringUtils;

// TODO patricia if stackmat isn't plugged in, have option to default to manually enter times
public class SolveActivity extends MicrophoneListenerActivity implements View.OnTouchListener {

    private static final String TAG = "SolveActivity";

    private static final String STOPWATCH_BUNDLE_KEY = "stopwatch_bundle_key";
    private static final String ISINSPECTING_BUNDLE_KEY = "is_inspecting_bundle_key";

    private static final int REFRESH_DISPLAY_FPS = 50;
    private static final int REFRESH_DISPLAY_INTERVAL_MILLIS = 1000/REFRESH_DISPLAY_FPS;
    private static final int VIBRATE_DURATION_MILLIS = 1000;

    private MicrophoneStatusReceiver mMicStatusReceiver;
    private Stopwatch mStopwatch;
    private Vibrator mVibrator;

    private Handler mHandler;
    private Runnable mUpdateDisplayRunnable;
    private Runnable mVibrateRunnable;

    private TextView mMicLevelView; // TODO patricia make this into a bar thingie
    private TextView mMicStatusView;
    private TextView mDisplayView;

    // true when in inspection mode, false otherwise
    private boolean mIsInspecting;
    private double mMicLevel;

    private class DisplayRunnable implements Runnable {

        @Override
        public void run() {
            int currentTime = FSKubeWrapper.getTimeMillis();
            if (mStopwatch.shouldRespondToStackmat() && FSKubeWrapper.isRunning()) {
                mIsInspecting = false;
                mHandler.removeCallbacks(mVibrateRunnable);
            }

            String textToDisplay;
            int colorId;
            if (mIsInspecting) {
                textToDisplay = mStopwatch.getTimeDisplay();
                colorId = mStopwatch.getTimerBackgroundColorId();
            } else {
                textToDisplay = StringUtils.getFormattedStackmatTime(currentTime);
                colorId = R.color.light_grey;
            }
            mDisplayView.setText(textToDisplay);
            mDisplayView.setBackgroundColor(getResources().getColor(colorId));
            mMicLevelView.setText("" + mMicLevel);
            mMicStatusView.setText("" + mMicStatusReceiver.isMicAvailable());

            mHandler.postDelayed(mUpdateDisplayRunnable, REFRESH_DISPLAY_INTERVAL_MILLIS);

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.solve_activity);

        mMicLevelView = (TextView) findViewById(R.id.microphone_level_textview);
        mMicStatusView = (TextView) findViewById(R.id.microphone_status_textview);
        mDisplayView = (TextView) findViewById(R.id.display_textview);
        mDisplayView.setOnTouchListener(this);

        mMicStatusReceiver = new MicrophoneStatusReceiver();
        IntentFilter receiverFilter = new IntentFilter(Intent.ACTION_HEADSET_PLUG);
        registerReceiver(mMicStatusReceiver, receiverFilter);

        mVibrator = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);

        mHandler = new Handler();
        mUpdateDisplayRunnable = new DisplayRunnable();
        mVibrateRunnable = new Runnable() {
            @Override
            public void run() {
                mVibrator.vibrate(VIBRATE_DURATION_MILLIS);
            }
        };

        if (savedInstanceState == null || !savedInstanceState.containsKey(STOPWATCH_BUNDLE_KEY)) {
            mStopwatch = new Stopwatch(this);
            mIsInspecting = true;
        } else {
            mStopwatch = savedInstanceState.getParcelable(STOPWATCH_BUNDLE_KEY);
            mIsInspecting = savedInstanceState.getBoolean(ISINSPECTING_BUNDLE_KEY);
        }

        FSKubeWrapper.initialize(MicrophoneListenerActivity.SAMPLE_RATE);
        FSKubeWrapper.setLogLevels("capi/*");
        mHandler.post(mUpdateDisplayRunnable);
        if (mIsInspecting && mStopwatch.isRunning()) {
            postVibrateRunnables();
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        // start timer if not running, else do nothing
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            mDisplayView.setTextColor(getTextColor(true));
            return true;
        }

        if (event.getAction() == MotionEvent.ACTION_UP) {
            mDisplayView.setTextColor(getTextColor(false));

            if (mIsInspecting && !mStopwatch.isRunning()) {
                if (!FSKubeWrapper.isOn()) {
                    Toast.makeText(getApplicationContext(),
                            getResources().getString(R.string.inspection_toast_turn_on_timer),
                            Toast.LENGTH_SHORT).show();
                } else if (FSKubeWrapper.getTimeMillis() != 0) {
                    Toast.makeText(getApplicationContext(),
                            getResources().getString(R.string.inspection_toast_reset_timer),
                            Toast.LENGTH_SHORT).show();
                } else {
                    mIsInspecting = true;
                    mStopwatch.reset();
                    mStopwatch.start();
                    postVibrateRunnables();
                }
            }

            return true;
        }
        return false;
    }

    @Override
    protected void onSaveInstanceState (Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelable(STOPWATCH_BUNDLE_KEY, mStopwatch);
        outState.putBoolean(ISINSPECTING_BUNDLE_KEY, mIsInspecting);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mHandler.removeCallbacks(mUpdateDisplayRunnable);
        mHandler.removeCallbacks(mVibrateRunnable);
    }

    @Override
    protected void handleSamples(double[] normalizedSamples) {
        double amplitude = Double.MIN_VALUE;
        for(double sample : normalizedSamples) {
            if (FSKubeWrapper.addSample(sample)) {
                // this fires when FSKube recognizes a valid time
                // this is a placeholder in case we need it later
            }
            amplitude = Math.max(amplitude, sample);
        }
        mMicLevel = amplitude;
    }

    private int getTextColor(boolean isPressed) {
        if (isPressed) {
            return getResources().getColor(R.color.grey);
        }
        return getResources().getColor(R.color.black);
    }

    private void postVibrateRunnables() {
        for (int warningTime : Stopwatch.WARNING_TIMES_MILLIS) {
            long delayed = warningTime - VIBRATE_DURATION_MILLIS
                    - mStopwatch.getElapsedTimeMillis();
            if (delayed > 0) {
                mHandler.postDelayed(mVibrateRunnable, delayed);
            }
        }
    }
}
