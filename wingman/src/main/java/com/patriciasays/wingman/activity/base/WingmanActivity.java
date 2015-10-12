package com.patriciasays.wingman.activity.base;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import com.patriciasays.wingman.CompetitionToolApp;
import com.patriciasays.wingman.util.Constants;

public class WingmanActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        String competitionName = sharedPref.getString(
                Constants.COMPETITION_ID_HUMAN_READABLE_PREFERENCE_KEY,
                CompetitionToolApp.getInstance().getApplicationName());
        setTitle(competitionName);
    }

}
