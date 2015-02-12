package com.patriciasays.wingman.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.patriciasays.wingman.data.Result;
import com.patriciasays.wingman.data.ResultWrapper;
import com.patriciasays.wingman.server.CCMClientApi;

public class TestActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        Result.SolveTime solveTime = new Result.SolveTime();
        solveTime.setMillis(4000);
        solveTime.setDecimals(2);
        solveTime.setPuzzlesSolvedCount(1);
        solveTime.setPuzzlesAttemptedCount(1);

        Result result = new Result();
        result.setRegistrationId("2CaHBnbSqkWaerRPs");
        result.setSolveTime(solveTime);
        result.setSolveIndex(0);

        ResultWrapper wrapper = new ResultWrapper();
        wrapper._id = "USNationals2014";
        wrapper.eventCode = "333";
        wrapper.nthRound = 4;
        wrapper.result = result;

        CCMClientApi.getInstance(this).submitTime(wrapper, null);

    }

}
