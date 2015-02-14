package com.patriciasays.wingman.activity.judge;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Spinner;
import android.widget.Toast;

import com.patriciasays.wingman.R;
import com.patriciasays.wingman.data.Participant;
import com.patriciasays.wingman.data.Result;
import com.patriciasays.wingman.data.ResultWrapper;
import com.patriciasays.wingman.data.Round;
import com.patriciasays.wingman.server.CCMClientApi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IdActivity extends Activity {

    private AutoCompleteTextView mSelectParticipantView;
    private Spinner mSelectRoundView;

    private Map<String, String> mParticipantNamesToIds;
    private List<String> mParticipantNames;
    private List<Round> mRounds;
    private List<String> mRoundNames;

    private ArrayAdapter<String> mParticipantAdapter;
    private ArrayAdapter<String> mRoundAdapter;

    private ResultWrapper mResultWrapper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.id_activity);

        mResultWrapper = new ResultWrapper();
        mResultWrapper.result = new Result();

        mSelectParticipantView = (AutoCompleteTextView) findViewById(R.id.participant_textview);
        mSelectRoundView = (Spinner) findViewById(R.id.select_round);

        mParticipantNamesToIds = new HashMap<String, String>();
        mParticipantNames = new ArrayList<String>();
        mRounds = new ArrayList<Round>();
        mRoundNames = new ArrayList<String>();

        mParticipantAdapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_dropdown_item_1line, mParticipantNames);
        mSelectParticipantView.setAdapter(mParticipantAdapter);

        mRoundAdapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, mRoundNames);
        mRoundAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSelectRoundView.setAdapter(mRoundAdapter);
        mSelectRoundView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mResultWrapper.eventCode = mRounds.get(position).getEventCode();
                mResultWrapper.nthRound = mRounds.get(position).getNthRound();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        sendRequests();
    }

    private void sendRequests() {
        CCMClientApi.getInstance(this).participants(new CCMClientApi.Listener<List<Participant>>() {
            @Override
            public void onResponse(List<Participant> response) {
                for (Participant p : response) {
                    mParticipantNames.add(p.getUniqueName());
                    mParticipantNamesToIds.put(p.getUniqueName().toLowerCase(), p.get_id());
                }
                mParticipantAdapter.notifyDataSetChanged();
            }
        });
        CCMClientApi.getInstance(this).rounds(new CCMClientApi.Listener<List<Round>>() {
            @Override
            public void onResponse(List<Round> response) {
                for (Round r : response) {
                    String displayString = String.format(
                            getResources().getString(R.string.select_round_display_string),
                            r.getEventCode(), r.getNthRound());
                    mRoundNames.add(displayString);
                    mRounds.add(r);
                }
                mRoundAdapter.notifyDataSetChanged();
            }
        });
    }

    public void next(View view) {
        // TODO query for solve index
        String participantName = String.valueOf(mSelectParticipantView.getText());
        mResultWrapper.result.setRegistrationId(
                mParticipantNamesToIds.get(participantName.toLowerCase()));

        Intent intent = new Intent(this, StackmatActivity.class);
        if (TextUtils.isEmpty(mResultWrapper.result.getRegistrationId())) {
            Toast.makeText(getApplicationContext(),
                    getResources().getString(R.string.select_competitor_toast),
                    Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(mResultWrapper.eventCode) || mResultWrapper.nthRound == 0) {
            Toast.makeText(getApplicationContext(),
                    getResources().getString(R.string.select_round_toast),
                    Toast.LENGTH_SHORT).show();
            return;
        }

        intent.putExtra(SubmitActivity.EXTRA_RESULT_WRAPPER, mResultWrapper);
        startActivity(intent);
    }

}
