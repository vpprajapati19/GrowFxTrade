package com.growfxtrade.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.growfxtrade.R;

import org.json.JSONObject;

public class RefundPolicyActivity extends AppCompatActivity implements View.OnClickListener {
    WebView browser;
    private Dialog dialog;
    private ImageView ivicon;
    JSONObject jsonObject;
    String title = "";
    private TextView tvtitle;
    String url = "";

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_refundpolicy);
        try {
            this.url = getIntent().getStringExtra("url");
            this.title = getIntent().getStringExtra("title");
        } catch (Exception unused) {
        }
        initComponent();
    }

    private void initComponent() {
        this.ivicon = (ImageView) findViewById(R.id.ivicon);
        this.tvtitle = (TextView) findViewById(R.id.tvtitle);
        WebView webView = (WebView) findViewById(R.id.webview);
        this.browser = webView;
        webView.getSettings().setJavaScriptEnabled(true);
        this.browser.getSettings().setUseWideViewPort(true);
        this.tvtitle.setText(this.title);
        this.ivicon.setOnClickListener(this);
        startWebView(this.url);
    }

    private void startWebView(String str) {
        this.browser.setWebViewClient(new WebViewClient() {
            ProgressDialog progressDialog;

            public boolean shouldOverrideUrlLoading(WebView webView, String str) {
                webView.loadUrl(str);
                return true;
            }

            public void onLoadResource(WebView webView, String str) {
                if (this.progressDialog == null) {
                    ProgressDialog progressDialog2 = new ProgressDialog(RefundPolicyActivity.this);
                    this.progressDialog = progressDialog2;
                    progressDialog2.setMessage("Loading...");
                    this.progressDialog.show();
                }
            }

            public void onPageFinished(WebView webView, String str) {
                try {
                    this.progressDialog.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                    this.progressDialog.dismiss();
                }
            }
        });
        this.browser.loadUrl(str);
    }

    public void onClick(View view) {
        if (view == this.ivicon) {
            finish();
            startActivity(new Intent(this, MainActivity.class));
            overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
        }
    }

    public void onBackPressed() {
        super.onBackPressed();
        finish();
        startActivity(new Intent(this, MainActivity.class));
        overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
    }
}
