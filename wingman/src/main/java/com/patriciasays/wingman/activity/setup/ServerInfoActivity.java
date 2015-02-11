package com.patriciasays.wingman.activity.setup;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.patriciasays.wingman.R;
import com.patriciasays.wingman.lib.DiscoverServerAsyncTask;
import com.patriciasays.wingman.util.Constants;

public class ServerInfoActivity extends Activity {

    private SharedPreferences mSharedPreferences;

    private TextView mServerInfoDomainNameView;
    private TextView mServerInfoPortNumberView;
    private Button mAutoDiscoverServerButton;
    private Button mEditServerInfoButton;
    private Button mUseDefaultsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.server_info_activity);

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        mServerInfoDomainNameView = (TextView) findViewById(R.id.server_info_domain_name);
        mServerInfoPortNumberView = (TextView) findViewById(R.id.server_info_port_number);
        mAutoDiscoverServerButton = (Button) findViewById(R.id.server_info_auto_discover);
        mEditServerInfoButton = (Button) findViewById(R.id.server_info_edit);
        mUseDefaultsButton = (Button) findViewById(R.id.server_info_use_defaults);

        refreshServerInfoDisplay();
        mAutoDiscoverServerButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DiscoverServerAsyncTask(view.getContext()).execute();
            }
        });
        mEditServerInfoButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                DialogFragment editDialogFragment = new EditServerInfoDialogFragment();
                editDialogFragment.show(transaction, "dialog");
            }
        });
        mUseDefaultsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                update(Constants.DEFAULT_SERVER_URL, Constants.DEFAULT_PORT);
                refreshServerInfoDisplay();
            }
        });
    }

    private void refreshServerInfoDisplay() {
        String serverInfoNotSet = getResources().getString(R.string.server_info_not_set);
        String domainNameString = getResources().getString(R.string.server_info_domain_name) + ": "
                + mSharedPreferences.getString(Constants.DOMAIN_NAME_PREFERENCE_KEY,serverInfoNotSet);
        String portNumberString = getResources().getString(R.string.server_info_port_number) + ": "
                + mSharedPreferences.getString(Constants.PORT_NUMBER_PREFERENCE_KEY, serverInfoNotSet);
        mServerInfoDomainNameView.setText(domainNameString);
        mServerInfoPortNumberView.setText(portNumberString);
    }

    @SuppressLint("ValidFragment")
    public class EditServerInfoDialogFragment extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            LayoutInflater inflater = getActivity().getLayoutInflater();

            View parent = inflater.inflate(R.layout.server_info_edit_dialog, null);
            builder.setView(parent);
            final EditText editDomainName = (EditText) parent.findViewById(R.id.edit_domain_name);
            final EditText editPortNumber = (EditText) parent.findViewById(R.id.edit_port_number);

            builder.setPositiveButton(R.string.submit, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            update(editDomainName.getText().toString(),
                                    editPortNumber.getText().toString());

                            refreshServerInfoDisplay();
                        }
                    })
                    .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            EditServerInfoDialogFragment.this.getDialog().cancel();
                        }
                    });
            return builder.create();
        }
    }

    public void next(View view) {
        Intent intent = new Intent(this, SelectCompetitionActivity.class);
        startActivity(intent);
    }

    private void update(String domainName, String portNumber) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(Constants.DOMAIN_NAME_PREFERENCE_KEY, domainName);
        editor.putString(Constants.PORT_NUMBER_PREFERENCE_KEY, portNumber);
        editor.commit();
    }
}
