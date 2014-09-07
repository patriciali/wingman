package com.patriciasays.wingman.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.patriciasays.wingman.R;
import com.patriciasays.wingman.util.Stopwatch;

public class InspectionActivity extends Activity implements View.OnTouchListener {

    private static final String TAG = "InspectionActivity";

    private static final int REFRESH_RATE_MILLIS = 50;
    private static final int VIBRATE_DURATION_MILLIS = 1000;

    private Stopwatch mStopwatch;
    private Vibrator mVibrator;
    private Handler mHandler;
    private Runnable mDisplayRunnable;
    private Runnable mVibrateRunnable;

    private TextView mInspectionEnterMessage;
    private TextView mInspectionTimeDisplay;

    private Stopwatch.TimerState mTimerState;
    private boolean mShouldStartOnNextUpTouch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inspection_activity);

        mInspectionEnterMessage = (TextView) findViewById(R.id.inspection_enter_message);
        mInspectionTimeDisplay = (TextView) findViewById(R.id.inspection_display);

        mInspectionEnterMessage.setOnTouchListener(this);
        mInspectionTimeDisplay.setOnTouchListener(this);

        mStopwatch = new Stopwatch();
        mVibrator = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
        mHandler = new Handler();
        mDisplayRunnable = new Runnable() {
            @Override
            public void run() {
                if (mStopwatch.isRunning()) {
                    updateDisplayAndColor();
                    mHandler.postDelayed(this, REFRESH_RATE_MILLIS);
                }
            }
        };
        mVibrateRunnable = new Runnable() {
            @Override
            public void run() {
                mVibrator.vibrate(VIBRATE_DURATION_MILLIS);
            }
        };

        mTimerState = Stopwatch.TimerState.NOT_RUNNING;
        mShouldStartOnNextUpTouch = true;
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            mInspectionTimeDisplay.setTextColor(getResources().getColor(R.color.grey));
            if (mStopwatch.isRunning()) {
                stopTimer();
                mShouldStartOnNextUpTouch = false;
            } else {
                mShouldStartOnNextUpTouch = true;
            }
            return true;
        }

        if (event.getAction() == MotionEvent.ACTION_UP) {
            mInspectionTimeDisplay.setTextColor(getResources().getColor(R.color.black));
            if (!mStopwatch.isRunning() && mShouldStartOnNextUpTouch) {
                startTimer();
            }
            return true;
        }

        return false;
    }

    private void startTimer() {
        mStopwatch = new Stopwatch();
        mInspectionEnterMessage.setVisibility(View.GONE);
        mInspectionTimeDisplay.setVisibility(View.VISIBLE);

        mStopwatch.start();
        mHandler.postDelayed(mDisplayRunnable, REFRESH_RATE_MILLIS);

        for (int warningTime : Stopwatch.WARNING_TIMES_MILLIS) {
            mHandler.postDelayed(mVibrateRunnable, warningTime - VIBRATE_DURATION_MILLIS);
        }
    }

    private void stopTimer() {
        mHandler.removeCallbacks(mDisplayRunnable);
        mHandler.removeCallbacks(mVibrateRunnable);
        if (mTimerState != Stopwatch.TimerState.DNF) {
            updateDisplayAndColor(); // so Jeremy can't thread the needle anymore
        }
        mStopwatch.stop();
        mTimerState = Stopwatch.TimerState.NOT_RUNNING;
    }

    private void updateDisplayAndColor() {
        if (mStopwatch.isRunning()) {
            mInspectionTimeDisplay.setText(mStopwatch.getElapsedTimeDisplay());
        }

        Stopwatch.TimerState timerState = mStopwatch.getTimerState();
        if (timerState != mTimerState) {
            mTimerState = timerState;

            switch (timerState) {
                case NOT_RUNNING:
                    break;

                case NO_WARNING:
                    mInspectionTimeDisplay.setBackgroundColor(
                            getResources().getColor(R.color.green));
                    break;

                case FIRST_WARNING:
                    mInspectionTimeDisplay.setBackgroundColor(
                            getResources().getColor(R.color.yellow));
                    break;

                case SECOND_WARNING:
                    mInspectionTimeDisplay.setBackgroundColor(
                            getResources().getColor(R.color.orange));
                    break;

                case PLUS_TWO:
                    mInspectionTimeDisplay.setBackgroundColor(
                            getResources().getColor(R.color.red));
                    break;

                case DNF:
                    mInspectionTimeDisplay.setText(
                            getResources().getString(R.string.inspection_dnf_message));
                    mInspectionTimeDisplay.setBackgroundColor(
                            getResources().getColor(R.color.dark_red));
                    stopTimer();
                    break;

                default:
                    Log.e(TAG, "updateDisplayAndColor encountered TimerState." + timerState
                            + "; not handled");
                    break;
            }
        }

    }

}
