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

    private static final String STOPWATCH_BUNDLE_KEY = "stopwatch_bundle_key";
    private static final String BACKGROUNDCOLOR_BUNDLE_KEY = "backgroundcolor_bundle_key";
    private static final String DISPLAYTEXT_BUNDLE_KEY = "displaytext_bundle_key";

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
    private int mBackgroundColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inspection_activity);

        mInspectionEnterMessage = (TextView) findViewById(R.id.inspection_enter_message);
        mInspectionTimeDisplay = (TextView) findViewById(R.id.inspection_display);

        mInspectionEnterMessage.setOnTouchListener(this);
        mInspectionTimeDisplay.setOnTouchListener(this);

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

        if (savedInstanceState == null || !savedInstanceState.containsKey(STOPWATCH_BUNDLE_KEY)) {
            mStopwatch = new Stopwatch();
            mBackgroundColor = getResources().getColor(R.color.green);
        } else {
            mInspectionEnterMessage.setVisibility(View.GONE);
            mInspectionTimeDisplay.setVisibility(View.VISIBLE);

            mStopwatch = savedInstanceState.getParcelable(STOPWATCH_BUNDLE_KEY);
            mBackgroundColor = savedInstanceState.getInt(BACKGROUNDCOLOR_BUNDLE_KEY);

            mInspectionTimeDisplay.setBackgroundColor(mBackgroundColor);
            mInspectionTimeDisplay.setText(savedInstanceState.getString(DISPLAYTEXT_BUNDLE_KEY));

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
        }
    }

    @Override
    protected void onSaveInstanceState (Bundle outState) {
        super.onSaveInstanceState(outState);

        if (mInspectionEnterMessage.getVisibility() == View.GONE) {
            outState.putParcelable(STOPWATCH_BUNDLE_KEY, mStopwatch);
            outState.putInt(BACKGROUNDCOLOR_BUNDLE_KEY, mBackgroundColor);
            outState.putString(DISPLAYTEXT_BUNDLE_KEY, (String) mInspectionTimeDisplay.getText());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        removeAllCallbacks();
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            mInspectionTimeDisplay.setTextColor(getTextColor(true));

            if (mStopwatch.isRunning()) {
                stopTimer();
                mShouldStartOnNextUpTouch = false;
            } else {
                mShouldStartOnNextUpTouch = true;
            }
            return true;
        }

        if (event.getAction() == MotionEvent.ACTION_UP) {
            mInspectionTimeDisplay.setTextColor(getTextColor(false));

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
        removeAllCallbacks();
        if (mTimerState != Stopwatch.TimerState.DNF) {
            updateDisplayAndColor(); // so Jeremy can't thread the needle anymore
        }
        mStopwatch.stop();
        mTimerState = Stopwatch.TimerState.NOT_RUNNING;
    }

    private void removeAllCallbacks() {
        mHandler.removeCallbacks(mDisplayRunnable);
        mHandler.removeCallbacks(mVibrateRunnable);
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
                    mBackgroundColor = getResources().getColor(R.color.green);
                    break;

                case FIRST_WARNING:
                    mBackgroundColor = getResources().getColor(R.color.yellow);
                    break;

                case SECOND_WARNING:
                    mBackgroundColor = getResources().getColor(R.color.orange);
                    break;

                case PLUS_TWO:
                    mBackgroundColor = getResources().getColor(R.color.red);
                    break;

                case DNF:
                    mInspectionTimeDisplay.setText(
                            getResources().getString(R.string.inspection_dnf_message));
                    mBackgroundColor = getResources().getColor(R.color.dark_red);
                    stopTimer();
                    break;

                default:
                    Log.e(TAG, "updateDisplayAndColor encountered TimerState." + timerState
                            + "; not handled");
                    break;
            }

            mInspectionTimeDisplay.setBackgroundColor(mBackgroundColor);
        }

    }

    private int getTextColor(boolean isPressed) {
        if (isPressed) {
            return getResources().getColor(R.color.grey);
        }
        return getResources().getColor(R.color.black);
    }

}
