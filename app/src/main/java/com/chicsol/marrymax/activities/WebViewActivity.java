package com.chicsol.marrymax.activities;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.chicsol.marrymax.R;

public class WebViewActivity extends AppCompatActivity {
    private Toolbar toolbar;
    String url;
    private WebView webView;
    private ProgressBar pDialog;
    private String title;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_web_view);
        pDialog = (ProgressBar) findViewById(R.id.ProgressbarProjectMain);
        pDialog.setVisibility(View.VISIBLE);

        url = getIntent().getStringExtra("url");
        title = getIntent().getStringExtra("title");


        toolbar = (Toolbar) findViewById(R.id.toolbar1);
        toolbar.setVisibility(View.VISIBLE);
        toolbar.setTitle(title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        webView = (WebView) findViewById(R.id.webView1);
/*        webView.setWebChromeClient(new WebChromeClient());*/

        WebSettings webSettings = webView.getSettings();
        webView.getSettings(). setDomStorageEnabled(true);
        webView.getSettings().setJavaScriptEnabled(true);

     webView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                pDialog.setVisibility(View.VISIBLE);

                if (progress == 100)
                    pDialog.setVisibility(View.GONE);
            }
        });


/*     webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(url);*/

      /*  webSettings.setDomStorageEnabled(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setDisplayZoomControls(false);
        webSettings.setSupportZoom(true);
        webSettings.setDefaultTextEncodingName("utf-8");*/


      webView.setWebViewClient(new myWebClient());

        webView.loadUrl(url);


  /*      webView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                //Make the bar disappear after URL is loaded, and changes string to Loading...
                // setTitle("Loading...");
                // setProgress(progress * 100); //Make the bar disappear after URL is loaded
                pDialog.setProgress(progress * 100);
                // Return the app name after finish loading
                if (progress == 100)
                    pDialog.setVisibility(View.GONE);
                //  setTitle(R.string.app_name);
            }
        });
        webView.setWebViewClient(new HelloWebViewClient());
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(url);*/


    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
            webView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    public class myWebClient extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            // TODO Auto-generated method stub
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            // TODO Auto-generated method stub
            pDialog.setVisibility(View.VISIBLE);
            view.loadUrl(url);
            return true;

        }

        @Override
        public void onPageFinished(WebView view, String url) {
            // TODO Auto-generated method stub
            super.onPageFinished(view, url);

            pDialog.setVisibility(View.GONE);
        }
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


}
