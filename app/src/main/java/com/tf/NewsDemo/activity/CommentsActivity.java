package com.tf.NewsDemo.activity;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.tf.NewsDemo.R;
import com.tf.NewsDemo.adapter.MyCommentAdapter;
import com.tf.NewsDemo.bean.Comment;
import com.tf.NewsDemo.bean.CommentListBean;
import com.tf.NewsDemo.utils.VolleyUtils;
import com.tf.NewsDemo.view.CommentsTitle;

import java.util.ArrayList;
import java.util.List;

public class CommentsActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{

    private String storyId;
    private String urls_long, urls_short;
    private List<Comment> commentList;
    private ListView listView_comments;
    private MyCommentAdapter myCommentAdapter;
    private CommentsTitle commentsTitle;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);
        init();
        initCommentTitle();
        Intent intent = getIntent();
        storyId = intent.getStringExtra("StoryId");
        urls_long = "http://news-at.zhihu.com/api/4/story/" + storyId + "/long-comments";
        urls_short = "http://news-at.zhihu.com/api/4/story/" + storyId + "/short-comments";

        getComments(urls_long);
        getComments(urls_short);
    }

    private void initCommentTitle() {
        commentsTitle= (CommentsTitle) findViewById(R.id.commentsTitle);
        ImageView imageView= (ImageView) commentsTitle.findViewById(R.id.commentsTitle_imageView_back);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void init() {
        listView_comments= (ListView) findViewById(R.id.listView_comments);
        commentList=new ArrayList<>();
        myCommentAdapter=new MyCommentAdapter(getApplicationContext(),commentList);
        listView_comments.setAdapter(myCommentAdapter);
        listView_comments.setOnItemClickListener(this);
    }

    public void getComments(String urls) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, urls,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        CommentListBean commentListBean= JSON.parseObject(response,CommentListBean.class);
                        for (Comment comment:commentListBean.getComments()){
                            commentList.add(comment);
                            myCommentAdapter.notifyDataSetChanged();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(CommentsActivity.this, "网络不可用", Toast.LENGTH_SHORT).show();
                    }
                });
        VolleyUtils.newInstance(getApplicationContext()).addQueue(stringRequest);
    }
// 弹出对话框并且成为焦点
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Comment comment=commentList.get(position);
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle(comment.getAuthor());
        builder.setMessage(comment.getContent());
        builder.show();
    }
}
