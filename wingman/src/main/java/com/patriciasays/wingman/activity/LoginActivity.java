package com.patriciasays.wingman.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;

import com.patriciasays.wingman.R;
import com.patriciasays.wingman.util.Constants;

public class LoginActivity extends Activity {

    private SharedPreferences mSharedPreferences;

    private WebView mGetTokenWebView;

    public class CCMJavascriptInterface {

        @JavascriptInterface
        public void jsSetToken(String token) {
            setToken(token);
        }

        @JavascriptInterface
        public void log(String str) {
            Log.d("CcmJsInterface.log", str);
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.login_activity);

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        mGetTokenWebView = (WebView) findViewById(R.id.get_token_webview);
        WebView.setWebContentsDebuggingEnabled(true);

        String url = Constants.DEFAULT_SERVER_URL + Constants.ROUTE_LOGIN;
        mGetTokenWebView.loadUrl(url);
        mGetTokenWebView.getSettings().setJavaScriptEnabled(true);
        mGetTokenWebView.addJavascriptInterface(
                new CCMJavascriptInterface(), "AndroidFunction");

        // this makes it so that links aren't opened in browser
        mGetTokenWebView.setWebViewClient(new WebViewClient());
    }

    private synchronized void setToken(String token) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(Constants.AUTH_TOKEN_PREFERENCE_KEY, token);
        editor.commit();

        SelectCompetitionActivity.start(this);
    }

}
