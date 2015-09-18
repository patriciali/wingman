package com.patriciasays.wingman.activity.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.patriciasays.wingman.R;
import com.patriciasays.wingman.activity.judge.IdActivity;

/**
 * this will be a launcher for everything until we figure out what the flow is going to be
 */
public class DashboardActivity extends Activity {

    private static final String TAG = "DashboardActivity";

    private static final String[] DASHBOARD_ITEMS = {
            "Setup flow",
            "Judging flow"};
    private static final Class<? extends Activity>[] DASHBOARD_ACTIVITIES = new Class[] {
            ServerInfoActivity.class,
            IdActivity.class};

    private ListView mListView;
    private ListAdapter mListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard_activity);

        mListView = (ListView) findViewById(R.id.dashboard_listview);
        mListAdapter =
                new ArrayAdapter<String>(this, R.layout.wingman_list_item, DASHBOARD_ITEMS);
        mListView.setAdapter(mListAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position > DASHBOARD_ITEMS.length || position > DASHBOARD_ACTIVITIES.length) {
                    Log.e(TAG, "Not enough items in dashboard array; position " + position);
                } else {
                    Context context = view.getContext();
                    Intent intent = new Intent(context, DASHBOARD_ACTIVITIES[position]);
                    context.startActivity(intent);
                }
            }
        });
    }

}
