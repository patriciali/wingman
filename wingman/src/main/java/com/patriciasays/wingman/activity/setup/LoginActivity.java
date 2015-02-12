package com.patriciasays.wingman.activity.setup;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.patriciasays.wingman.R;
import com.patriciasays.wingman.util.Constants;

public class LoginActivity extends Activity {

    private SharedPreferences mSharedPreferences;

    private EditText mUsernameView;
    private EditText mPasswordView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.login_activity);

        mUsernameView = (EditText) findViewById(R.id.username_textview);
        mPasswordView = (EditText) findViewById(R.id.password_textview);

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
    }

    public void login(View view) {
        // TODO WEBVIEW
        setToken("zk8HiOoe7Pn8LM_0tMDm3IuT9N6pIofkJtH4qrarl3j");
    }

    private void setToken(String token) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(Constants.AUTH_TOKEN_PREFERENCE_KEY, token);
        editor.commit();
    }

}
