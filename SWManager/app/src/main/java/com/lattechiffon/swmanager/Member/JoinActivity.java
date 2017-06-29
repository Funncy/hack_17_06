package com.lattechiffon.swmanager.Member;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.webkit.JavascriptInterface;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.lattechiffon.swmanager.R;

public class JoinActivity extends AppCompatActivity {

    SharedPreferences pref;

    @SuppressLint({"JavascriptInterface", "SetJavaScriptEnabled"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        final Handler handler = new Handler();
        pref = getSharedPreferences("UserData", Activity.MODE_PRIVATE);

        final class JavaScriptExtention {

            private JavaScriptExtention() {
            }

            @JavascriptInterface
            public void requestListener(final String arg) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        switch (arg) {
                            case "JoinSuccess":
                                Snackbar.make(findViewById(R.id.joinWebView), "가입이 완료되었습니다.", Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();
                                JoinActivity.this.finish();

                                break;
                        }
                    }
                });

            }
        }

        WebView webView = (WebView) findViewById(R.id.joinWebView);

        webView.setWebViewClient(new JoinActivity.WebClient());

        WebSettings set = webView.getSettings();
        set.setJavaScriptEnabled(true);
        webView.addJavascriptInterface(new JavaScriptExtention(), "android");

        webView.loadUrl("http://mattmatt96.dothome.co.kr/join.html");

    }

    class WebClient extends WebViewClient {
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            view.loadUrl(request.getUrl().toString());

            return true;
        }
    }
}