package com.patriciasays.wingman.activity.judge;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.jflei.fskube.FSKubeWrapper;
import com.patriciasays.wingman.R;
import com.patriciasays.wingman.CompetitionToolApp;
import com.patriciasays.wingman.data.ResultWrapper;
import com.patriciasays.wingman.activity.common.MicrophoneListenerActivity;
import com.patriciasays.wingman.lib.Stopwatch;
import com.patriciasays.wingman.util.StringUtils;

public class StackmatActivity extends MicrophoneListenerActivity implements View.OnTouchListener {

    private static final String TAG = "StackmatActivity";

    private static final String STOPWATCH_BUNDLE_KEY = "stopwatch_bundle_key";
    private static final String ISINSPECTING_BUNDLE_KEY = "is_inspecting_bundle_key";

    private static final int REFRESH_DISPLAY_FPS = 50;
    private static final int REFRESH_DISPLAY_INTERVAL_MILLIS = 1000/REFRESH_DISPLAY_FPS;
    private static final int VIBRATE_DURATION_MILLIS = 1000;

    private ResultWrapper mResultWrapper;

    private Stopwatch mStopwatch;
    private Vibrator mVibrator;

    private Handler mHandler;
    private Runnable mUpdateDisplayRunnable;
    private Runnable mVibrateRunnable;

    private TextView mMicLevelView; // TODO pzl make this into a bar thingie
    private TextView mMicStatusView;
    private TextView mDisplayView;
    private Button mSubmitTimeButton;

    // true when in inspection mode, false otherwise
    private boolean mIsInspecting;
    private double mMicLevel;

    private boolean mHasInspectionPenalty;

    private class DisplayRunnable implements Runnable {

        @Override
        public void run() {
            int currentTime = FSKubeWrapper.getTimeMillis();
            if (mStopwatch.shouldRespondToStackmat() && FSKubeWrapper.isRunning()) {
                mIsInspecting = false;
                mHandler.removeCallbacks(mVibrateRunnable);

                mHasInspectionPenalty = Stopwatch.shouldPenalize(mStopwatch.getElapsedTimeMillis());
            }

            String textToDisplay;
            int colorId;
            if (mIsInspecting) {
                textToDisplay = mStopwatch.getTimeDisplay();
                colorId = mStopwatch.getTimerBackgroundColorId();
            } else {
                textToDisplay = StringUtils.getFormattedStackmatTime(currentTime);
                colorId = R.color.light_grey;
            }
            mDisplayView.setText(textToDisplay);
            mDisplayView.setBackgroundColor(getResources().getColor(colorId));
            mMicLevelView.setText("" + mMicLevel);
            mMicStatusView.setText("" + CompetitionToolApp.getInstance().getMicConnectedStatus());

            if ((mStopwatch.isRunning() && !mIsInspecting && !FSKubeWrapper.isRunning()) ||
                    TextUtils.equals(mStopwatch.getDnfMessage(), textToDisplay)) {
                mSubmitTimeButton.setVisibility(View.VISIBLE);
            }

            mHandler.postDelayed(mUpdateDisplayRunnable, REFRESH_DISPLAY_INTERVAL_MILLIS);

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stackmat_activity);

        mResultWrapper = getIntent().getParcelableExtra(SubmitActivity.EXTRA_RESULT_WRAPPER);

        mMicLevelView = (TextView) findViewById(R.id.microphone_level_textview);
        mMicStatusView = (TextView) findViewById(R.id.microphone_status_textview);
        mDisplayView = (TextView) findViewById(R.id.display_textview);
        mSubmitTimeButton = (Button) findViewById(R.id.goto_submitactivity_button);
        mDisplayView.setOnTouchListener(this);

        mVibrator = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);

        mHandler = new Handler();
        mUpdateDisplayRunnable = new DisplayRunnable();
        mVibrateRunnable = new Runnable() {
            @Override
            public void run() {
                mVibrator.vibrate(VIBRATE_DURATION_MILLIS);
            }
        };

        if (savedInstanceState == null || !savedInstanceState.containsKey(STOPWATCH_BUNDLE_KEY)) {
            mStopwatch = new Stopwatch(this);
            mIsInspecting = true;
        } else {
            mStopwatch = savedInstanceState.getParcelable(STOPWATCH_BUNDLE_KEY);
            mIsInspecting = savedInstanceState.getBoolean(ISINSPECTING_BUNDLE_KEY);
        }

        FSKubeWrapper.initialize(SAMPLE_RATE);
        FSKubeWrapper.setLogLevels("capi/*,digitizer/*");
        mHandler.post(mUpdateDisplayRunnable);
        if (mIsInspecting && mStopwatch.isRunning()) {
            postVibrateRunnables();
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        // start timer if not running, else do nothing
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            mDisplayView.setTextColor(getTextColor(true));
            return true;
        }

        if (event.getAction() == MotionEvent.ACTION_UP) {
            mDisplayView.setTextColor(getTextColor(false));

            if (mIsInspecting && !mStopwatch.isRunning()) {
                if (!FSKubeWrapper.isOn()) {
                    Toast.makeText(getApplicationContext(),
                            getResources().getString(R.string.inspection_toast_turn_on_timer),
                            Toast.LENGTH_SHORT).show();
                } else if (FSKubeWrapper.getTimeMillis() != 0) {
                    Toast.makeText(getApplicationContext(),
                            getResources().getString(R.string.inspection_toast_reset_timer),
                            Toast.LENGTH_SHORT).show();
                }

                mIsInspecting = true;
                mStopwatch.reset();
                mStopwatch.start();
                postVibrateRunnables();
            }

            return true;
        }
        return false;
    }

    @Override
    protected void onSaveInstanceState (Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelable(STOPWATCH_BUNDLE_KEY, mStopwatch);
        outState.putBoolean(ISINSPECTING_BUNDLE_KEY, mIsInspecting);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mHandler.removeCallbacks(mUpdateDisplayRunnable);
        mHandler.removeCallbacks(mVibrateRunnable);
    }

    @Override
    protected void handleSamples(double[] normalizedSamples) {
        double amplitude = Double.MIN_VALUE;
        for(double sample : normalizedSamples) {
            if (FSKubeWrapper.addSample(sample)) {
                // this fires when FSKube recognizes a valid time
                // this is a placeholder in case we need it later
            }
            amplitude = Math.max(amplitude, sample);
        }
        mMicLevel = amplitude;
    }

    public void next(View view) {
        submit(mDisplayView.getText().toString(), (long) FSKubeWrapper.getTimeMillis());
    }

    public void manualEnterTime(View view) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        DialogFragment editDialogFragment = new ManuallyEnterTimeFragment();
        editDialogFragment.show(transaction, "dialog");
    }

    private void submit(String resultDisplay, long resultMillis) {
        Intent intent = new Intent(this, SubmitActivity.class);
        if (TextUtils.equals(resultDisplay, mStopwatch.getDnfMessage())) {
            intent.putExtra(SubmitActivity.EXTRA_SOLVE_RESULT, SubmitActivity.DNF_RESULT);
        } else {
            intent.putExtra(SubmitActivity.EXTRA_SOLVE_RESULT, resultMillis);
        }
        intent.putExtra(SubmitActivity.EXTRA_HAS_INSPECTION_PENALTY, mHasInspectionPenalty);
        intent.putExtra(SubmitActivity.EXTRA_RESULT_WRAPPER, mResultWrapper);
        startActivity(intent);
    }

    private int getTextColor(boolean isPressed) {
        if (isPressed) {
            return getResources().getColor(R.color.grey);
        }
        return getResources().getColor(R.color.black);
    }

    private void postVibrateRunnables() {
        for (int warningTime : Stopwatch.WARNING_TIMES_MILLIS) {
            long delayed = warningTime - VIBRATE_DURATION_MILLIS
                    - mStopwatch.getElapsedTimeMillis();
            if (delayed > 0) {
                mHandler.postDelayed(mVibrateRunnable, delayed);
            }
        }
    }

    @SuppressLint("ValidFragment")
    class ManuallyEnterTimeFragment extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            LayoutInflater inflater = getActivity().getLayoutInflater();

            View parent = inflater.inflate(R.layout.manually_enter_time_dialog, null);
            builder.setView(parent);
            final EditText timeEditText = (EditText) parent.findViewById(R.id.time_edit_text);

            builder.setPositiveButton(R.string.submit, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {
                    submit("TODO", Integer.parseInt(timeEditText.getText().toString()));
                }
            })
                    .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            ManuallyEnterTimeFragment.this.getDialog().cancel();
                        }
                    });
            return builder.create();
        }
    }
}
