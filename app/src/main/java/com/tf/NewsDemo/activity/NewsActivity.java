package com.tf.NewsDemo.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;
import com.tf.NewsDemo.R;
import com.tf.NewsDemo.bean.ListStory;
import com.tf.NewsDemo.bean.NewsDetial;
import com.tf.NewsDemo.bean.TopStory;
import com.tf.NewsDemo.utils.BitmapCache;
import com.tf.NewsDemo.utils.VolleyUtils;
import com.tf.NewsDemo.view.NewsTitle;

public class NewsActivity extends AppCompatActivity{

    private WebView webView;
    private TopStory topStory;
    private ListStory listStory;
    private String storyId;
    private NewsDetial newsDetial;
    private ImageView imageView_newsDetial;
    private TextView textView_title,textView_picAuthor;
    private NewsTitle newsTitle;
    private ImageView imageView_newsTitle_back,imageView_newsTitle_pingLun;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        init();
        initTitleListener();
        Intent intent=getIntent();
        if (intent.getBooleanExtra("isTop",false)){
            topStory=intent.getParcelableExtra("story");
        }else {
            listStory=intent.getParcelableExtra("story");
        }

        getNewsDetial();


    }

    private void initTitleListener() {
        imageView_newsTitle_back= (ImageView) newsTitle.findViewById(R.id.newsTitle_imageView_back);
        imageView_newsTitle_pingLun= (ImageView) newsTitle.findViewById(R.id.newsTitle_pingLun);
        imageView_newsTitle_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        imageView_newsTitle_pingLun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),CommentsActivity.class);
                if (topStory!=null){
                    intent.putExtra("StoryId",topStory.getId());
                }else {
                    intent.putExtra("StoryId",listStory.getId());
                }
                startActivity(intent);
            }
        });

    }

    private void init() {
        webView= (WebView) findViewById(R.id.webView);
        imageView_newsDetial= (ImageView) findViewById(R.id.imageView_newsDetial_newsPic);
        textView_title= (TextView) findViewById(R.id.textView_newsDetial_title);
        textView_picAuthor= (TextView) findViewById(R.id.textView_newsDetial_picAuthor);
        newsTitle= (NewsTitle) findViewById(R.id.newsTitle_activity);
    }

    public void getNewsDetial() {
        if (topStory!=null){
            storyId=topStory.getId();
        }else {
            storyId=listStory.getId();
        }
        String urls = "http://news-at.zhihu.com/api/4/news/"+storyId;
        StringRequest stringRequest=new StringRequest(Request.Method.GET, urls,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        newsDetial= JSON.parseObject(response,NewsDetial.class);
                        getImage();
                        textView_title.setText(newsDetial.getTitle());
                        textView_picAuthor.setText(newsDetial.getImage_source());
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

    private void getWebViewData() {
        String body=newsDetial.getBody();
        String html = "<html><head>" + "</head><body>" + body+ "</body></html>";
        html = html.replace("<div class=\"img-place-holder\">", "");
        //这是对出文章提供者头像之外的图片，全部自适应屏幕大小的操作
        html=html.replace("<img","<img style=\"width:100%;height:auto\" ");
        html=html.replaceFirst("<img style=\"width:100%;height:auto\" ","<img");
        webView.loadDataWithBaseURL("x-data://base", html, "text/html", "UTF-8", null);
        //让webView支持java脚本
        webView.getSettings().setJavaScriptEnabled(true);
    }

    public void getImage() {
        ImageLoader.ImageListener listener = ImageLoader.getImageListener(imageView_newsDetial, R.mipmap.ic_launcher, R.mipmap.ic_launcher);
        //使用缓存的工具类
        ImageLoader imageLoader = new ImageLoader(VolleyUtils.newInstance(getApplicationContext()).getRequestQueue(), new BitmapCache());
        imageLoader.get(newsDetial.getImage(), listener);
    }
}
