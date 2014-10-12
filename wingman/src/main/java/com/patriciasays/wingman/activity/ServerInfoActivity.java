package com.patriciasays.wingman.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.patriciasays.wingman.R;

public class ServerInfoActivity extends Activity implements Button.OnClickListener {

    private static final String DOMAIN_NAME_PREFERENCE_KEY = "domain_name_key";
    private static final String PORT_NUMBER_PREFERENCE_KEY = "port_number_key";

    private SharedPreferences mSharedPreferences;

    private TextView mServerInfoDomainNameView;
    private TextView mServerInfoPortNumberView;
    private Button mEditServerInfoButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.server_info_activity);

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        mServerInfoDomainNameView = (TextView) findViewById(R.id.server_info_domain_name);
        mServerInfoPortNumberView = (TextView) findViewById(R.id.server_info_port_number);
        mEditServerInfoButton = (Button) findViewById(R.id.server_info_edit);

        refreshServerInfoDisplay();
        mEditServerInfoButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        DialogFragment editDialogFragment = new EditServerInfoDialogFragment();
        editDialogFragment.show(transaction, "dialog");
    }

    private void refreshServerInfoDisplay() {
        String serverInfoNotSet = getResources().getString(R.string.server_info_not_set);
        String domainNameString = getResources().getString(R.string.server_info_domain_name) + ": "
                + mSharedPreferences.getString(DOMAIN_NAME_PREFERENCE_KEY, serverInfoNotSet);
        String portNumberString = getResources().getString(R.string.server_info_port_number) + ": "
                + mSharedPreferences.getString(PORT_NUMBER_PREFERENCE_KEY, serverInfoNotSet);
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
                            SharedPreferences.Editor editor = mSharedPreferences.edit();
                            editor.putString(DOMAIN_NAME_PREFERENCE_KEY,
                                    editDomainName.getText().toString());
                            editor.putString(PORT_NUMBER_PREFERENCE_KEY,
                                    editPortNumber.getText().toString());
                            editor.commit();

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
}
