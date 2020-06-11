package com.example.appiii.ui.Gmap;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.appiii.R;

public class ActWebView extends Activity {
    Bundle bundle;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_webview);
        bundle = getIntent().getExtras();
        String searquery = bundle.getString("search");
        WVInitialComponent();
        view_webview.loadUrl("https://www.google.com/search?q="+searquery);
    }

    private void WVInitialComponent() {
        view_webview = findViewById(R.id.view_webview);
        WebSettings webSettings = view_webview.getSettings();
        webSettings.setJavaScriptEnabled(true);  // 取得網頁JS效果
        webSettings.setDefaultTextEncodingName("utf-8");
        webSettings.setBuiltInZoomControls(true);       //是否支持手指縮放

    }

    WebView view_webview;
}
