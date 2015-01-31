package com.patriciasays.wingman.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.patriciasays.wingman.R;

public class SubmitActivity extends Activity {

    public static final String EXTRA_SOLVE_RESULT = "extra_solve_result";
    public static final String EXTRA_HAS_INSPECTION_PENALTY = "has_inspection_penalty";

    private TextView mResultView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.submit_activity);

        mResultView = (TextView) findViewById(R.id.result_textview);

        Intent intent = getIntent();
        String result = intent.getStringExtra(EXTRA_SOLVE_RESULT);
        mResultView.setText(result);
    }

    public void subtractPenalty(View view) {
        // TODO
    }

    public void addPenalty(View view) {
        // TODO
    }

    public void applyDnf(View view) {
        // TODO
    }

    public void submit(View view) {
        // TODO
    }

}
