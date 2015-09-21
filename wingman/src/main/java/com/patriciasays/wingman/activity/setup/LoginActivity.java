package com.patriciasays.wingman.activity.setup;

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
            Log.d("fuck", "token " + token);
            setToken(token);
        }

        @JavascriptInterface
        public void log(String str) {
            Log.d("log_fuck", str);
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.login_activity);

        mGetTokenWebView = (WebView) findViewById(R.id.get_token_webview);

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        WebView.setWebContentsDebuggingEnabled(true);

        String url = mSharedPreferences.getString(
                Constants.URL_PREFERENCE_KEY, Constants.DEFAULT_SERVER_URL);
        mGetTokenWebView.loadUrl(url);
        mGetTokenWebView.getSettings().setJavaScriptEnabled(true);
        mGetTokenWebView.addJavascriptInterface(
                new CCMJavascriptInterface(), "AndroidFunction");

        mGetTokenWebView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageFinished(WebView view, String url) {

            }

        });
    }

    private synchronized void setToken(String token) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(Constants.AUTH_TOKEN_PREFERENCE_KEY, token);
        editor.commit();
    }

}
