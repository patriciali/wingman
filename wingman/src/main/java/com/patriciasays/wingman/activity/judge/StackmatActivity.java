package com.patriciasays.wingman.activity.judge;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.patriciasays.wingman.R;
import com.patriciasays.wingman.activity.common.NeedsNetworkActivity;
import com.patriciasays.wingman.data.ResultWrapper;
import com.patriciasays.wingman.lib.Stopwatch;

public class StackmatActivity extends NeedsNetworkActivity implements View.OnTouchListener {

    private static final String TAG = "StackmatActivity";

    private static final int REFRESH_DISPLAY_FPS = 50;
    private static final int REFRESH_DISPLAY_INTERVAL_MILLIS = 1000/REFRESH_DISPLAY_FPS;
    private static final int VIBRATE_DURATION_MILLIS = 1000;

    private ResultWrapper mResultWrapper;

    private Stopwatch mStopwatch;
    private Vibrator mVibrator;

    private Handler mHandler;
    private Runnable mUpdateDisplayRunnable;
    private Runnable mVibrateRunnable;

    private TextView mDisplayView;

    private class DisplayRunnable implements Runnable {

        @Override
        public void run() {
            String textToDisplay;
            int colorId;
            textToDisplay = mStopwatch.getTimeDisplay();
            colorId = mStopwatch.getTimerBackgroundColorId();

            mDisplayView.setText(textToDisplay);
            mDisplayView.setBackgroundColor(mDisplayView.getResources().getColor(colorId));

            mHandler.postDelayed(mUpdateDisplayRunnable, REFRESH_DISPLAY_INTERVAL_MILLIS);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stackmat_activity);

        mResultWrapper = getIntent().getParcelableExtra(SubmitActivity.EXTRA_RESULT_WRAPPER);

        mDisplayView = (TextView) findViewById(R.id.display_textview);
        mDisplayView.setOnTouchListener(this);

        mVibrator = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);

        mHandler = new Handler();
        mUpdateDisplayRunnable = new DisplayRunnable();
        mVibrateRunnable = new Runnable() {
            @Override
            public void run() {
                mVibrator.vibrate(VIBRATE_DURATION_MILLIS);
            }
        };

        mStopwatch = new Stopwatch(this);

        mHandler.post(mUpdateDisplayRunnable);
        if (mStopwatch.isRunning()) {
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

            if (!mStopwatch.isRunning()) {
                mStopwatch.reset();
                mStopwatch.start();
                postVibrateRunnables();
            }

            return true;
        }
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mHandler.removeCallbacks(mUpdateDisplayRunnable);
        mHandler.removeCallbacks(mVibrateRunnable);
    }

    public void next(View view) {
        submit(mDisplayView.getText().toString());
    }

    private void submit(String resultDisplay) {
        Intent intent = new Intent(this, SubmitActivity.class);
        intent.putExtra(SubmitActivity.EXTRA_RESULT_WRAPPER, mResultWrapper);
        startActivity(intent);
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
