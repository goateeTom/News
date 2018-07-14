package com.tf.NewsDemo.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.tf.NewsDemo.R;
import com.tf.NewsDemo.bean.NewsDetial;
import com.tf.NewsDemo.utils.VolleyUtils;
import com.tf.NewsDemo.view.NewsTitle;

public class ThemeActivity extends AppCompatActivity {

    private NewsTitle themeTitle_activity;
    private WebView webView_themeActivity;
    private String storyId;
    private NewsDetial newsDetial;
    private ImageView imageView_newsTitle_back, imageView_newsTitle_pingLun;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theme);
        init();
        initTitleListener();
        storyId = getIntent().getStringExtra("StoryId");
        getThemeDetial();
    }

    private void init() {
        themeTitle_activity = (NewsTitle) findViewById(R.id.themeTitle_activity);
        webView_themeActivity = (WebView) findViewById(R.id.webView_themeActivity);
    }

    public void getThemeDetial() {
        String urls = "http://news-at.zhihu.com/api/4/news/" + storyId;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, urls,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        newsDetial = JSON.parseObject(response, NewsDetial.class);
                        getWebViewData();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "网络不可用", Toast.LENGTH_SHORT).show();
                    }
                });
        VolleyUtils.newInstance(getApplicationContext()).addQueue(stringRequest);

    }

    private void initTitleListener() {
        imageView_newsTitle_back = (ImageView) themeTitle_activity.findViewById(R.id.newsTitle_imageView_back);
        imageView_newsTitle_pingLun = (ImageView) themeTitle_activity.findViewById(R.id.newsTitle_pingLun);
        imageView_newsTitle_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        imageView_newsTitle_pingLun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CommentsActivity.class);
                intent.putExtra("StoryId", storyId);
                startActivity(intent);
            }
        });

    }

    private void getWebViewData() {
        String body = newsDetial.getBody();
        String html = "<html><head>" + "</head><body>" + body + "</body></html>";
        html = html.replace("<div class=\"img-place-holder\">", "");
       //这是对出文章提供者头像之外的图片，全部自适应屏幕大小的操作
        html = html.replace("<img", "<img style=\"width:100%;height:auto\" ");
       html = html.replaceFirst("<img style=\"width:100%;height:auto\" ", "<img");
        webView_themeActivity.loadDataWithBaseURL("x-data://base", html, "text/html", "UTF-8", null);
        //让我们的webView支持java脚本
        webView_themeActivity.getSettings().setJavaScriptEnabled(true);
    }


}
