package com.patriciasays.wingman.activity;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.patriciasays.wingman.R;
import com.patriciasays.wingman.server.ServerApi;

import java.util.List;

public class ApiTestActivity extends Activity {

    private LinearLayout mCompetitionsListLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.api_test_activity);

        mCompetitionsListLayout = (LinearLayout) findViewById(R.id.competitions_list);
        new RetrieveAndPopulateCompetitionsAsyncTask().execute();
    }

    private class RetrieveAndPopulateCompetitionsAsyncTask extends
            AsyncTask<Void, Void, List<String>> {

        @Override
        protected List<String> doInBackground(Void... voids) {
            return ServerApi.getCompetitionsList(ApiTestActivity.this);
        }

        @Override
        protected void onPostExecute(List<String> competitionsList) {
            for (String competitionName : competitionsList) {
                TextView entry = new TextView(ApiTestActivity.this);
                entry.setText(competitionName);
                mCompetitionsListLayout.addView(entry);
            }
        }
    }

}
