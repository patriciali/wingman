package com.patriciasays.wingman.activity.setup;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;

import com.patriciasays.wingman.R;
import com.patriciasays.wingman.util.Constants;

public class LoginActivity extends Activity {

    private SharedPreferences mSharedPreferences;

    private EditText mUsernameView;
    private EditText mPasswordView;
    private Button mLoginButton;
    private WebView mGetTokenWebView;

    public class CCMJavascriptInterface {

        private SharedPreferences mJsSharedPreferences;

        public CCMJavascriptInterface(SharedPreferences sharedPref) {
            mJsSharedPreferences = sharedPref;
        }

        public void jsSetToken(String token) {
            setToken(token);
        }

        public void log(String str) {
            Log.d("AndroidFunction.log_fuck", str);
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.login_activity);

        mUsernameView = (EditText) findViewById(R.id.username_textview);
        mPasswordView = (EditText) findViewById(R.id.password_textview);
        mLoginButton = (Button) findViewById(R.id.login_button);
        mGetTokenWebView = (WebView) findViewById(R.id.get_token_webview);

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        String url = mSharedPreferences.getString(
                Constants.URL_PREFERENCE_KEY, Constants.DEFAULT_SERVER_URL);
        mGetTokenWebView.loadUrl(url);
        mGetTokenWebView.getSettings().setJavaScriptEnabled(true);
        mGetTokenWebView.addJavascriptInterface(
                new CCMJavascriptInterface(mSharedPreferences), "AndroidFunction");
        mGetTokenWebView.setWebViewClient(new WebViewClient() {
            public void onPageFinished(WebView view, String url) {
                mLoginButton.setVisibility(View.VISIBLE);
            }
        });
    }

    public void login(View view) {
        String username = mUsernameView.getText().toString();
        String password = mPasswordView.getText().toString();

        String js = String.format(Constants.GET_AUTH_TOKEN_JS, username, password);
        mGetTokenWebView.loadUrl("javascript:" + js);
    }

    private synchronized void setToken(String token) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(Constants.AUTH_TOKEN_PREFERENCE_KEY, token);
        editor.commit();
    }

}
