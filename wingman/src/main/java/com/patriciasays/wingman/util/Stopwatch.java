package com.patriciasays.wingman.util;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.patriciasays.wingman.R;

public class Stopwatch implements Parcelable {

    private static final String TAG = "Stopwatch";

    private static final int MAX_INSPECTION_TIME_MILLIS = 17000;
    public static final int[] WARNING_TIMES_MILLIS = new int[] {8000, 12000, 15000};

    private static final int STATE_NOT_STARTED = 0;
    private static final int STATE_RUNNING = 1;

    private long mStartTime;
    private long mStopTime;
    private int mState;

    public Stopwatch() {
        this(0, 0, STATE_NOT_STARTED);
    }

    private Stopwatch(long startTime, long stopTime, int state) {
        mStartTime = startTime;
        mStopTime = stopTime;
        mState = state;
    }

    public void start() {
        mStartTime = System.currentTimeMillis();
        mState = STATE_RUNNING;

    }

    public void reset() {
        mState = STATE_NOT_STARTED;
    }

    public boolean isRunning() {
        return mState == STATE_RUNNING;
    }

    public String getTimeDisplay() {
        if (mState == STATE_NOT_STARTED) {
            return "0.0";
        } else {
            float elapsed = getElapsedTimeMillis();
            if (elapsed >= MAX_INSPECTION_TIME_MILLIS) {
                return "DNF"; // TODO patricia string me
            }
            return String.format("%.1f", elapsed / 1000);
        }
    }

    public int getTimerBackgroundColorId() {
        long elapsed = getElapsedTimeMillis();
        if (elapsed >= MAX_INSPECTION_TIME_MILLIS) {
            return R.color.dark_red;
        }
        if (elapsed >= WARNING_TIMES_MILLIS[2]) {
            return R.color.red;
        }
        if (elapsed >= WARNING_TIMES_MILLIS[1]) {
            return R.color.orange;
        }
        if (elapsed >= WARNING_TIMES_MILLIS[0]) {
            return R.color.yellow;
        }
        return R.color.green;
    }

    public boolean shouldRespondToStackmat() {
        if (mState == STATE_NOT_STARTED || getElapsedTimeMillis() >= MAX_INSPECTION_TIME_MILLIS) {
            return false;
        }

        return true;
    }

    private long getElapsedTimeMillis() {
        long elapsed;
        if (isRunning()) {
            elapsed = (System.currentTimeMillis() - mStartTime);
        } else {
            elapsed = (mStopTime - mStartTime);
        }
        return elapsed;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(mStartTime);
        dest.writeLong(mStopTime);
        dest.writeInt(mState);
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Stopwatch createFromParcel(Parcel in) {
            long startTime = in.readLong();
            long stopTime = in.readLong();
            int state = in.readInt();

            return new Stopwatch(startTime, stopTime, state);
        }

        public Stopwatch[] newArray(int size) {
            return new Stopwatch[size];
        }
    };

}