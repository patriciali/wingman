package com.patriciasays.wingman.util;

import android.os.Parcel;
import android.os.Parcelable;

public class Stopwatch implements Parcelable {

    public enum TimerState {
        NOT_RUNNING,
        NO_WARNING,
        FIRST_WARNING,
        SECOND_WARNING,
        PLUS_TWO,
        DNF;
    }

    private static final int MAX_INSPECTION_TIME_MILLIS = 17000;

    public static final int[] WARNING_TIMES_MILLIS = new int[] {8000, 12000, 15000};

    private long mStartTime;
    private long mStopTime;
    private boolean mIsRunning;

    public Stopwatch() {
        this(0, 0, false);
    }

    private Stopwatch(long startTime, long stopTime, boolean isRunning) {
        mStartTime = startTime;
        mStopTime = stopTime;
        mIsRunning = isRunning;
    }

    public void start() {
        this.mStartTime = System.currentTimeMillis();
        this.mIsRunning = true;
    }

    public void stop() {
        this.mStopTime = System.currentTimeMillis();
        this.mIsRunning = false;
    }

    public boolean isRunning() {
        return mIsRunning;
    }

    public long getElapsedTimeMillis() {
        long elapsed;
        if (mIsRunning) {
            elapsed = (System.currentTimeMillis() - mStartTime);
        } else {
            elapsed = (mStopTime - mStartTime);
        }
        return elapsed;
    }

    public String getElapsedTimeDisplay() {
        float elapsed = getElapsedTimeMillis();
        return String.format("%.1f", elapsed / 1000);
    }

    public TimerState getTimerState() {
        if (!mIsRunning) {
            return TimerState.NOT_RUNNING;
        }

        long elapsed = getElapsedTimeMillis();
        if (elapsed >= MAX_INSPECTION_TIME_MILLIS) {
            return TimerState.DNF;
        }
        if (elapsed >= WARNING_TIMES_MILLIS[2]) {
            return TimerState.PLUS_TWO;
        }
        if (elapsed >= WARNING_TIMES_MILLIS[1]) {
            return TimerState.SECOND_WARNING;
        }
        if (elapsed >= WARNING_TIMES_MILLIS[0]) {
            return TimerState.FIRST_WARNING;
        }
        return TimerState.NO_WARNING;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(mStartTime);
        dest.writeLong(mStopTime);
        dest.writeBooleanArray(new boolean[]{mIsRunning});
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Stopwatch createFromParcel(Parcel in) {
            long startTime = in.readLong();
            long stopTime = in.readLong();
            boolean[] isRunning = new boolean[1];
            in.readBooleanArray(isRunning);

            return new Stopwatch(startTime, stopTime, isRunning[0]);
        }

        public Stopwatch[] newArray(int size) {
            return new Stopwatch[size];
        }
    };

}