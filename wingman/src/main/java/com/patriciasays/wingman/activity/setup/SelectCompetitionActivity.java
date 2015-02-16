package com.patriciasays.wingman.activity.setup;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.patriciasays.wingman.R;
import com.patriciasays.wingman.activity.util.DashboardActivity;
import com.patriciasays.wingman.data.Competition;
import com.patriciasays.wingman.activity.common.NeedsNetworkActivity;
import com.patriciasays.wingman.server.CCMClientApi;
import com.patriciasays.wingman.util.Constants;

import java.util.ArrayList;
import java.util.List;

public class SelectCompetitionActivity extends NeedsNetworkActivity {

    private static final String TAG = "SelectCompetitionActivity";

    private SharedPreferences mSharedPreferences;

    private ListView mListView;
    private ListAdapter mListAdapter;

    private List<Competition> mCompetitions;

    private View mPreviouslySelectedView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_competition_activity);

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mCompetitions = new ArrayList<Competition>();

        mListView = (ListView) findViewById(R.id.competitions_listview);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position > mCompetitions.size()) {
                    Log.e(TAG, "Not enough items in competitions list; position " + position);
                } else {
                    SharedPreferences.Editor editor = mSharedPreferences.edit();
                    editor.putString(Constants.COMPETITION_ID_PREFERENCE_KEY,
                            mCompetitions.get(position).get_id());
                    editor.putString(Constants.COMPETITION_ID_HUMAN_READABLE_PREFERENCE_KEY,
                            mCompetitions.get(position).getCompetitionName());
                    editor.commit();

                    updateSelectedButtonBackground(view, true);
                    if (mPreviouslySelectedView != null) {
                        updateSelectedButtonBackground(mPreviouslySelectedView, false);
                    }
                    mPreviouslySelectedView = view;
                }
            }
        });

        CCMClientApi.Listener<List<Competition>> listener =
                new CCMClientApi.Listener<List<Competition>>() {
            @Override
            public void onResponse(List<Competition> response) {
                mCompetitions = response;

                List<String> competitionNames = new ArrayList<String>();
                for (Competition comp : mCompetitions) {
                    competitionNames.add(comp.getCompetitionName());
                }
                mListAdapter = new ArrayAdapter<String>(SelectCompetitionActivity.this,
                        R.layout.wingman_list_item, competitionNames);
                mListView.setAdapter(mListAdapter);
            }
        };
        CCMClientApi.getInstance(this).competitionsList(listener);
    }

    public void finish(View view) {
        // TODO
        Intent intent = new Intent(this, DashboardActivity.class);
        startActivity(intent);
    }

    private void updateSelectedButtonBackground(View view, boolean selected) {
        if (selected) {
            view.setBackgroundColor(getResources().getColor(R.color.green));
        } else {
            view.setBackground(null);
        }
    }

}
