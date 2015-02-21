package com.patriciasays.wingman.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Wrapper around {@link com.patriciasays.wingman.data.Result} with extra fields eventCode, nthRound
 */
public class ResultWrapper implements Parcelable {

    public String eventCode;
    public int nthRound;
    public Result result;

    public ResultWrapper() {

    }

    public ResultWrapper(String eventCode_, int nthRound_, Result result_) {
        this.eventCode = eventCode_;
        this.nthRound = nthRound_;
        this.result = result_;
    }

    @Override
    public String toString() {
        return "eventCode: " + this.eventCode + ", nthRound: " + this.nthRound + ", result: " + this.result.toString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(eventCode);
        dest.writeInt(nthRound);
        if (result != null) {
            dest.writeString(result.getRegistrationId());
            dest.writeInt(result.getSolveIndex());
        }
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public ResultWrapper createFromParcel(Parcel in) {
            String eventCode_ = in.readString();
            int nthRound_ = in.readInt();
            Result result_ = new Result(in.readString(), in.readInt());

            return new ResultWrapper(eventCode_, nthRound_, result_);
        }

        @Override
        public ResultWrapper[] newArray(int i) {
            return new ResultWrapper[0];
        }
    };
}
