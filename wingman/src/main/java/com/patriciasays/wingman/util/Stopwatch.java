package com.patriciasays.wingman.util;

import android.content.Context;
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
    private String mDnfMessage;

    public Stopwatch(Context context) {
        this(0, 0, STATE_NOT_STARTED,
                context.getResources().getString(R.string.inspection_dnf_message));
    }

    private Stopwatch(long startTime, long stopTime, int state, String dnfMessage) {
        mStartTime = startTime;
        mStopTime = stopTime;
        mState = state;
        mDnfMessage = dnfMessage;
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
            long elapsed = getElapsedTimeMillis();
            if (elapsed >= MAX_INSPECTION_TIME_MILLIS) {
                return mDnfMessage;
            }
            return StringUtils.getFormattedInspectionTime(elapsed);
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

    public long getElapsedTimeMillis() {
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
        dest.writeString(mDnfMessage);
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Stopwatch createFromParcel(Parcel in) {
            long startTime = in.readLong();
            long stopTime = in.readLong();
            int state = in.readInt();
            String dnfMessage = in.readString();

            return new Stopwatch(startTime, stopTime, state, dnfMessage);
        }

        public Stopwatch[] newArray(int size) {
            return new Stopwatch[size];
        }
    };

}