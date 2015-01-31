package com.patriciasays.wingman.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.patriciasays.wingman.R;
import com.patriciasays.wingman.util.Constants;
import com.patriciasays.wingman.util.StringUtils;

public class SubmitActivity extends Activity {

    public static final String EXTRA_SOLVE_RESULT = "extra_solve_result";
    public static final String EXTRA_HAS_INSPECTION_PENALTY = "has_inspection_penalty";

    public static final long DNF_RESULT = -1;
    private static final int DNF_NUM_PENALTIES = -1;

    private TextView mResultCalculationView;
    private TextView mResultView;

    private long mResultTime;
    private int mNumPenalties;
    private int mNumPenaltiesCache; // if judge accidentally presses DNF, we use this to get it back

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.submit_activity);

        mResultCalculationView = (TextView) findViewById(R.id.result_calculation_textview);
        mResultView = (TextView) findViewById(R.id.result_textview);

        mNumPenalties = 0;

        Intent intent = getIntent();
        mResultTime = intent.getLongExtra(EXTRA_SOLVE_RESULT, DNF_RESULT);
        updateResult(mNumPenalties);

        if (intent.getBooleanExtra(EXTRA_HAS_INSPECTION_PENALTY, false)) {
            addPenalty(null);
        }
    }

    public void subtractPenalty(View view) {
        if (mResultTime == DNF_RESULT || mNumPenalties == DNF_NUM_PENALTIES) {
            Toast.makeText(getApplicationContext(),
                    getResources().getString(R.string.penalty_failure_dnf),
                    Toast.LENGTH_SHORT).show();
            return;
        }

        if (mNumPenalties == 0) {
            Toast.makeText(getApplicationContext(),
                    getResources().getString(R.string.subtract_penalty_failure_none),
                    Toast.LENGTH_SHORT).show();
            return;
        }

        updateResult(mNumPenalties - 1);
    }

    // TODO check what is the maximum number of penalties under WCA regs?
    public void addPenalty(View view) {
        if (mResultTime == DNF_RESULT || mNumPenalties == DNF_NUM_PENALTIES) {
            Toast.makeText(getApplicationContext(),
                    getResources().getString(R.string.penalty_failure_dnf),
                    Toast.LENGTH_SHORT).show();
            return;
        }

        updateResult(mNumPenalties + 1);
    }

    public void applyDnf(View view) {
        if (mNumPenalties == DNF_NUM_PENALTIES) {
            updateResult(mNumPenaltiesCache);
        } else {
            updateResult(DNF_NUM_PENALTIES);
        }
    }

    public void submit(View view) {
        // TODO
    }

    private void updateResult(int numPenalties) {
        mNumPenalties = numPenalties;
        if (mNumPenalties != DNF_NUM_PENALTIES) {
            mNumPenaltiesCache = mNumPenalties;
        }

        mResultCalculationView.setText(getResultCalculationString());
        mResultView.setText(getDisplayResult());
    }

    /**
     * @return if mResultTime = 9250 and mNumPenalties = 1, returns "9.25 + 2 ="
     */
    private String getResultCalculationString() {
        String calc = "";
        if (mResultTime == DNF_RESULT || mNumPenalties == DNF_NUM_PENALTIES || mNumPenalties == 0) {
            return calc;
        }

        String plusTwo = getResources().getString(R.string.plus_two);
        calc += StringUtils.getFormattedStackmatTime(mResultTime) + " ";
        for (int i = 0; i < mNumPenalties; i += 1) {
            calc += plusTwo + " ";
        }
        calc += "=";
        return calc;
    }

    /**
     * @return if mResultTime = 9250 and mNumPenalties = 1, returns "11.25"
     */
    private String getDisplayResult() {
        if (mResultTime == DNF_RESULT || mNumPenalties == DNF_NUM_PENALTIES) {
            return getResources().getString(R.string.dnf);
        }

        long penalizedTime = mResultTime + mNumPenalties * Constants.PENALTY_TIME_MILLIS;
        return StringUtils.getFormattedStackmatTime(penalizedTime);
    }

}
