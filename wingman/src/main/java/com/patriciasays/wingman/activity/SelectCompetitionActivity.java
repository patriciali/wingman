package com.patriciasays.wingman.activity;

import android.app.Activity;
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
import com.patriciasays.wingman.data.Competition;
import com.patriciasays.wingman.activity.base.NeedsNetworkActivity;
import com.patriciasays.wingman.util.Constants;

import java.util.ArrayList;
import java.util.List;

public class SelectCompetitionActivity extends NeedsNetworkActivity {

    private static final String TAG = "SelectCompetition";

    private SharedPreferences mSharedPreferences;

    private ListView mListView;
    private ListAdapter mListAdapter;

    private List<Competition> mCompetitions;

    private View mPreviouslySelectedView;

    public static void start(Activity caller) {
        Intent intent = new Intent(caller, SelectCompetitionActivity.class);
        caller.startActivity(intent);
    }

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
    }

    private void updateSelectedButtonBackground(View view, boolean selected) {
        if (selected) {
            view.setBackgroundColor(getResources().getColor(R.color.green));
        } else {
            view.setBackground(null);
        }
    }

}
