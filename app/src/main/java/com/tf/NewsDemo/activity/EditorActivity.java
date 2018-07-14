package com.tf.NewsDemo.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import com.tf.NewsDemo.R;
import com.tf.NewsDemo.view.EditorTitle;

public class EditorActivity extends AppCompatActivity {

    private WebView webView;
    private EditorTitle editorTitle;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
        init();
        webView.loadUrl(getIntent().getStringExtra("urls"));
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }
        });
    }

    private void init() {
        webView= (WebView) findViewById(R.id.webView_editorActivity);
        editorTitle= (EditorTitle) findViewById(R.id.editorTitle);
        ImageView imageView= (ImageView) editorTitle.findViewById(R.id.editorTitle_imageView_back);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    //对返回按键进行判断，同时判断webView是否有可以回退的网页，若有，就返回上一个你浏览过的网页
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
            webView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
