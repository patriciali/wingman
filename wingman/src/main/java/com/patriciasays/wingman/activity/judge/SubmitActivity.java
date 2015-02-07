package com.patriciasays.wingman.activity.judge;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.patriciasays.wingman.R;
import com.patriciasays.wingman.util.Constants;
import com.patriciasays.wingman.util.StringUtils;

public class SubmitActivity extends Activity {

    public static final String EXTRA_SOLVE_RESULT = "extra_solve_result";
    public static final String EXTRA_HAS_INSPECTION_PENALTY = "has_inspection_penalty";

    public static final int INSPECTION_PENALTY_INDEX = 4;
    public static final long DNF_RESULT = -1;

    private String[] mHumanReadablePenalties;
    private boolean[] mPenalties;

    private TextView mResultCalculationView;
    private TextView mResultView;
    private ListView mPenaltyButtonsListView;

    private long mResultTime;
    private long mResultTimeCache; // if judge presses DNF accidentally we use this to get it back

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.submit_activity);

        mResultCalculationView = (TextView) findViewById(R.id.result_calculation_textview);
        mResultView = (TextView) findViewById(R.id.result_textview);
        mPenaltyButtonsListView = (ListView) findViewById(R.id.penalty_buttons_list);

        mHumanReadablePenalties = getResources().getStringArray(R.array.penalties_human_readable);
        mPenalties = new boolean[mHumanReadablePenalties.length];

        mPenaltyButtonsListView.setAdapter(new ArrayAdapter<String>(this,
                R.layout.submit_activity_penalty_button, mHumanReadablePenalties));
        mPenaltyButtonsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mResultTime != DNF_RESULT) {
                    mPenalties[position] = !mPenalties[position];

                    if (mPenalties[position]) {
                        view.getBackground().setColorFilter(
                                getResources().getColor(R.color.red), PorterDuff.Mode.MULTIPLY);
                    } else {
                        view.getBackground().setColorFilter(null);
                    }

                    updateDisplay();
                }
            }
        });

        Intent intent = getIntent();
        mResultTime = intent.getLongExtra(EXTRA_SOLVE_RESULT, DNF_RESULT);
        mResultTimeCache = mResultTime;
        if (intent.getBooleanExtra(EXTRA_HAS_INSPECTION_PENALTY, false)) {
            // TODO need custom adapter
        }
        updateDisplay();
    }

    public void applyDnf(View view) {
        if (mResultTime == mResultTimeCache) {
            mResultTime = DNF_RESULT;
        } else {
            mResultTime = mResultTimeCache;
        }

        updateDisplay();
    }

    public void submit(View view) {
        // TODO
    }

    private void updateDisplay() {
        mResultCalculationView.setText(getResultCalculationString());
        mResultView.setText(getDisplayResult());
    }

    /**
     * @return if mResultTime = 9250 and mNumPenalties = 1, returns "9.25 +2 ="
     */
    private String getResultCalculationString() {
        String calc = "";
        if (mResultTime == DNF_RESULT || countPenalties() == 0) {
            return calc;
        }

        String plusTwo = getResources().getString(R.string.plus_two);
        calc += StringUtils.getFormattedStackmatTime(mResultTime) + " ";
        for (int i = 0; i < countPenalties(); i += 1) {
            calc += plusTwo + " ";
        }
        calc += "=";
        return calc;
    }

    /**
     * @return if mResultTime = 9250 and mNumPenalties = 1, returns "11.25"
     */
    private String getDisplayResult() {
        if (mResultTime == DNF_RESULT) {
            return getResources().getString(R.string.dnf);
        }

        long penalizedTime = mResultTime + countPenalties() * Constants.PENALTY_TIME_MILLIS;
        return StringUtils.getFormattedStackmatTime(penalizedTime);
    }

    private int countPenalties() {
        int count = 0;
        for (boolean penalty : mPenalties) {
            if (penalty) {
                count += 1;
            }
        }
        return count;
    }

}
