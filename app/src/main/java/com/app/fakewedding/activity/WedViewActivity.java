package com.app.fakewedding.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.app.fakewedding.R;
import com.app.fakewedding.databinding.ActivityWedViewBinding;

public class WedViewActivity extends AppCompatActivity {
    WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wed_view);
        webView = findViewById(R.id.webView);
        webView.setWebViewClient(new WebViewClient());
        webView.getSettings().setJavaScriptEnabled(true);

        String url = getIntent().getStringExtra("URL");
        if (url != null) {
            webView.loadUrl(url);
        }
    }
}