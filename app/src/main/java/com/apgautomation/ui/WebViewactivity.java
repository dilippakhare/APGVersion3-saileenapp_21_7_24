package com.apgautomation.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

import com.apgautomation.R;

public class WebViewactivity extends AppCompatActivity {


    WebView webview;
    public static String token="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);

        webview=(WebView)findViewById(R.id.webView1);
        WebSettings setting= webview.getSettings();
        setting.setJavaScriptEnabled(true);

        setting.setDomStorageEnabled(true);
        setting.setDatabaseEnabled(true);

        webview.loadUrl("http://174.141.238.216/FSRAPG/#/landing?id="+token);


        webview.setWebViewClient(new WebViewClient()
        {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);

            }
        });

        webview.setWebViewClient(new WEBCLIENT("http://174.141.238.216/FSRAPG/#/landing?id="+token,this));

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("FSR Details");
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    boolean loadingFinished,redirect;
    class WEBCLIENT extends  WebViewClient
    {
        String ur;
        ProgressDialog pd=new ProgressDialog(WebViewactivity.this);
        public WEBCLIENT(String ur, Activity c)
        {
            // TODO Auto-generated constructor stub
            pd=new ProgressDialog(c);
            pd.setMessage("Loading");
            pd.show();
            this.ur=ur;
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String urlNewString)
        {
            if (!loadingFinished)
            {
                redirect = true;
            }

            loadingFinished = false;
            view.loadUrl(urlNewString);
            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap facIcon)
        {
            loadingFinished = false;
            //SHOW LOADING IF IT ISNT ALREADY VISIBLE
            if(!pd.isShowing())
                pd.show();
           //
            // CommonShare.alert(WebViewactivity.this,url);
            if(url.contains("http://174.141.238.216/app/privacy.html"))
            {
               WebViewactivity.this. finish();
            }
        }

        @Override
        public void onPageFinished(WebView view, String url)
        {

            if(!redirect)
            {
                loadingFinished = true;
            }

            if(loadingFinished && !redirect)
            {
                //HIDE LOADING IT HAS FINISHED

                // storeToDB(ur);
                pd.dismiss();

            }
            else
            {
                redirect = false;
            }

        }


    }

}
