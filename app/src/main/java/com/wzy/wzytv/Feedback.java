package com.wzy.wzytv;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class Feedback extends AppCompatActivity {

    private WebView webview_feedback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        initView();
    }

    private void initView() {
        webview_feedback = (WebView) findViewById(R.id.webview_feedback);
        webview_feedback.loadUrl("http://www.wzyxzy.tk:8080/feedback.html");
        webview_feedback.setWebViewClient(new ExampleWebViewClient());
    }

    private class ExampleWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return false;
        }
    }
}
