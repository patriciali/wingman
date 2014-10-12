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

import com.jflei.fskube.FSKubeWrapper;
import com.patriciasays.wingman.R;
import com.patriciasays.wingman.util.MicrophoneStatusReceiver;
import com.patriciasays.wingman.util.Stopwatch;

public class SolveActivity extends MicrophoneListenerActivity implements View.OnTouchListener {

    private static final String TAG = "SolveActivity";

    //private static final String STOPWATCH_BUNDLE_KEY = "stopwatch_bundle_key";
    //private static final String DISPLAYTEXT_BUNDLE_KEY = "displaytext_bundle_key";

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
    private int mLastStackmatTime;

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

        mStopwatch = new Stopwatch();
        mVibrator = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);

        mHandler = new Handler();
        mUpdateDisplayRunnable = new Runnable() {
            @Override
            public void run() {
                int currentTime = FSKubeWrapper.getTimeMillis();
                if (mStopwatch.shouldRespondToStackmat() && currentTime != mLastStackmatTime) {
                    mIsInspecting = false;
                }
                mLastStackmatTime = currentTime;

                String textToDisplay;
                int colorId;
                if (mIsInspecting) {
                    textToDisplay = mStopwatch.getTimeDisplay();
                    colorId = mStopwatch.getTimerBackgroundColorId();
                } else {
                    textToDisplay = "" + String.format("%.3f", (currentTime + 0.0) / 1000);
                    colorId = R.color.light_grey;
                }
                mDisplayView.setText(textToDisplay);
                mDisplayView.setBackgroundColor(getResources().getColor(colorId));
                mMicLevelView.setText("" + mMicLevel);
                mMicStatusView.setText("" + mMicStatusReceiver.isMicAvailable());

                mHandler.postDelayed(mUpdateDisplayRunnable, REFRESH_DISPLAY_INTERVAL_MILLIS);
            }
        };
        mVibrateRunnable = new Runnable() {
            @Override
            public void run() {
                mVibrator.vibrate(VIBRATE_DURATION_MILLIS);
            }
        };

        mIsInspecting = true;

        FSKubeWrapper.initialize(MicrophoneListenerActivity.SAMPLE_RATE);
        mHandler.post(mUpdateDisplayRunnable);

        /*if (savedInstanceState == null || !savedInstanceState.containsKey(STOPWATCH_BUNDLE_KEY)) {
            mStopwatch = new Stopwatch();
            mBackgroundColor = getResources().getColor(R.color.green);
        } else {
            mDisplayView.setVisibility(View.VISIBLE);

            mStopwatch = savedInstanceState.getParcelable(STOPWATCH_BUNDLE_KEY);
            mBackgroundColor = savedInstanceState.getInt(BACKGROUNDCOLOR_BUNDLE_KEY);

            mDisplayView.setBackgroundColor(mBackgroundColor);
            mDisplayView.setText(savedInstanceState.getString(DISPLAYTEXT_BUNDLE_KEY));

            if (mStopwatch.isRunning()) {
                mHandler.postDelayed(mDisplayRunnable, REFRESH_RATE_MILLIS);
                for (int warningTime : Stopwatch.WARNING_TIMES_MILLIS) {
                    long delayed = warningTime - VIBRATE_DURATION_MILLIS
                            - mStopwatch.getElapsedTimeMillis();
                    if (delayed > 0) {
                        mHandler.postDelayed(mVibrateRunnable, delayed);
                    }
                }
            }
        }*/
    }

    @Override
    protected void onSaveInstanceState (Bundle outState) {
        super.onSaveInstanceState(outState);

        //outState.putParcelable(STOPWATCH_BUNDLE_KEY, mStopwatch);
        //outState.putString(DISPLAYTEXT_BUNDLE_KEY, (String) mDisplayView.getText());
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
            }
            amplitude = Math.max(amplitude, sample);
        }
        mMicLevel = amplitude;
    }

    private void onStackmatStart() {
        if (mIsInspecting && mStopwatch.isRunning()) {
            mIsInspecting = false;
        }
    }

    private void onStackmatStop() {
        // TODO patricia
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        // start timer if not running, else do nothing for now
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            mDisplayView.setTextColor(getTextColor(true));
            return true;
        }

        if (event.getAction() == MotionEvent.ACTION_UP) {
            mDisplayView.setTextColor(getTextColor(false));

            //if (mIsInspecting && !mStopwatch.isRunning()) {
            mIsInspecting = true;
            mStopwatch.reset();
            mStopwatch.start();
            //}

            return true;
        }
        return false;
    }

    private int getTextColor(boolean isPressed) {
        if (isPressed) {
            return getResources().getColor(R.color.grey);
        }
        return getResources().getColor(R.color.black);
    }
}
