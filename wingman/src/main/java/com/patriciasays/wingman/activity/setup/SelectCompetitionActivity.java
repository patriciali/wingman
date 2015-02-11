package com.patriciasays.wingman.activity.setup;

import android.app.Activity;
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
import com.patriciasays.wingman.server.CCMClientApi;
import com.patriciasays.wingman.server.ResponseWrapper;
import com.patriciasays.wingman.util.Constants;

import java.util.ArrayList;
import java.util.List;

public class SelectCompetitionActivity extends Activity {

    private static final String TAG = "SelectCompetitionActivity";

    private SharedPreferences mSharedPreferences;

    private ListView mListView;
    private ListAdapter mListAdapter;

    private List<String> mCompetitions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_competition_activity);

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mCompetitions = new ArrayList<String>();

        mListView = (ListView) findViewById(R.id.competitions_listview);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position > mCompetitions.size()) {
                    Log.e(TAG, "Not enough items in competitions list; position " + position);
                } else {
                    SharedPreferences.Editor editor = mSharedPreferences.edit();
                    // TODO this should actually be ID and not name
                    editor.putString(
                            Constants.COMPETITION_ID_PREFERENCE_KEY, mCompetitions.get(position));
                    editor.commit();
                }
            }
        });

        ResponseWrapper.Listener<List<String>> listener =
            new ResponseWrapper.Listener<List<String>>() {
                @Override
                public void onResponse(List<String> response) {
                    mCompetitions = response;
                    mListAdapter = new ArrayAdapter<String>(SelectCompetitionActivity.this,
                            R.layout.wingman_list_item, mCompetitions);
                    mListView.setAdapter(mListAdapter);
                }
            };
        CCMClientApi.getInstance(this).competitionsList(
                ResponseWrapper.getCompetitionsListWrapper(listener));
    }

    public void finish(View view) {
        // TODO
    }

}
