package com.patriciasays.wingman.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.patriciasays.wingman.R;

public class ServerInfoActivity extends Activity implements Button.OnClickListener {

    private static final String DOMAIN_NAME_PREFERENCE_KEY = "domain_name_key";
    private static final String PORT_NUMBER_PREFERENCE_KEY = "port_number_key";

    private SharedPreferences mSharedPreferences;

    private EditText mServerInfoDomainNameView;
    private EditText mServerInfoPortNumberView;
    private Button mServerInfoSubmitButton;
    // TODO patricia have some way to view current preferences

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.server_info_activity);

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        mServerInfoDomainNameView = (EditText) findViewById(R.id.server_info_domain_name);
        mServerInfoPortNumberView = (EditText) findViewById(R.id.server_info_port_number);
        mServerInfoSubmitButton = (Button) findViewById(R.id.server_info_submit);
        mServerInfoSubmitButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(DOMAIN_NAME_PREFERENCE_KEY,
                mServerInfoDomainNameView.getText().toString());
        editor.putString(PORT_NUMBER_PREFERENCE_KEY,
                mServerInfoPortNumberView.getText().toString());
        editor.commit();
    }
}
