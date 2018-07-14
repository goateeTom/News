package com.tf.NewsDemo.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.tf.NewsDemo.R;
import com.tf.NewsDemo.bean.Theme;
import com.tf.NewsDemo.bean.ThemeAllBean;
import com.tf.NewsDemo.db.KnowDB;
import com.tf.NewsDemo.fragment.MainFragment;
import com.tf.NewsDemo.fragment.ThemeFragment;
import com.tf.NewsDemo.utils.VolleyUtils;
import com.tf.NewsDemo.view.MainTitle;
import com.tf.NewsDemo.view.SlidingMenuTheme;

import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private SlidingMenu slidingMenu;
    private MainTitle mainTitle;
    private RelativeLayout relativeLayout_main;
    private boolean isNight = false;
    private FragmentManager manager;
    private Fragment mainFragment,themeFragment;
    private RelativeLayout relativeLayout;
    private GridLayout gridLayout_slidingMenu;
    private TextView textView_mainTitle;
    private UpdateSlidingMenuThemReceiver updateSlidingMenuThemReceiver;
    private IntentFilter intentFilter;
    private Intent intent=null;
    private RelativeLayout backgroud=null;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        initSlidingMenu();
        initMainTitle();

        manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.fragment_main, mainFragment);
        transaction.commit();
        getSlidingThemeData();
    }

    private void init() {
        relativeLayout_main = (RelativeLayout) findViewById(R.id.relativeLayout_main);
        updateSlidingMenuThemReceiver=new UpdateSlidingMenuThemReceiver();
        intentFilter=new IntentFilter();
        intentFilter.addAction("UpdateSlidingMenuThem");
        registerReceiver(updateSlidingMenuThemReceiver,intentFilter);
    }
       //标题栏初始化
    private void initMainTitle() {
        mainTitle = (MainTitle) findViewById(R.id.mainTitle);
        ImageView imageView_home = (ImageView) mainTitle.findViewById(R.id.mainTitle_imageView_home);
        imageView_home.setOnClickListener(this);
        ImageView imageView_setting = (ImageView) mainTitle.findViewById(R.id.mainTitle_imageView_setting);
        imageView_setting.setOnClickListener(this);
        ImageView imageView_login = (ImageView) mainTitle.findViewById(R.id.mainTitle_imageView_login);
        imageView_login.setOnClickListener(this);
        ImageView imageView_registered = (ImageView) mainTitle.findViewById(R.id.mainTitle_imageView_registered);
        imageView_registered.setOnClickListener(this);
        backgroud=(RelativeLayout)findViewById(R.id.mainTitle_backgroud);
        textView_mainTitle= (TextView) mainTitle.findViewById(R.id.mainTitle_textView_title);
        textView_mainTitle.setText("首页");
        mainFragment = new MainFragment();
    }
//侧滑菜单初始化
    private void initSlidingMenu() {
        slidingMenu = new SlidingMenu(this);
        //设置滑动菜单在左边
        slidingMenu.setMode(SlidingMenu.LEFT);
        // 设置触摸拖动模式为边缘拖动
        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
        //设置阴影的宽度为15dp
        slidingMenu.setShadowWidthRes(R.dimen.shadow_width);
        // 设置滑动菜单视图的宽度为100dp
        slidingMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        // 设置颜色渐变比例为0.35f
        slidingMenu.setFadeDegree(0.35f);
        /**
         * SLIDING_WINDOW will include the Title/ActionBar in the content
         * section of the SlidingMenu, while SLIDING_CONTENT does not.
         */
        slidingMenu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
        //为侧滑菜单设置布局
        slidingMenu.setMenu(R.layout.layout_slidingmenu);
        //添加点击事件
        relativeLayout = (RelativeLayout) slidingMenu.findViewById(R.id.slidingMenu_home);
        relativeLayout.setOnClickListener(this);
        gridLayout_slidingMenu = (GridLayout) slidingMenu.findViewById(R.id.gridLayout_slidingMenu);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mainTitle_imageView_home:
                slidingMenu.toggle();
                break;
            //夜间模式
            case R.id.mainTitle_imageView_setting:
                if (isNight) {
                    relativeLayout_main.setBackgroundColor(Color.rgb(255, 255, 255));
                    backgroud.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    isNight = false;
                } else {
                    relativeLayout_main.setBackgroundColor(Color.rgb(72, 71, 71));
                    backgroud.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                    isNight = true;
                }
                break;
            //注册
            case R.id.mainTitle_imageView_registered:
                 intent=new Intent(MainActivity.this, LoginActivity.class);
                 intent.putExtra("TEMP",1);
                startActivityForResult(intent,1);
                break;
            //登录
            case R.id.mainTitle_imageView_login:
                 intent=new Intent(MainActivity.this, LoginActivity.class);
                intent.putExtra("TEMP",0);
                startActivityForResult(intent,0);
                break;
            //侧拉框
            case R.id.slidingMenu_home:
                mainFragment = new MainFragment();
                FragmentTransaction transaction_home = manager.beginTransaction();
                transaction_home.replace(R.id.fragment_main, mainFragment);
                transaction_home.commit();
                textView_mainTitle.setText("首页");
                slidingMenu.toggle();
                break;
        }
    }

    //用于获取侧滑菜单的栏目信息的方法
    public void getSlidingThemeData() {
        if (KnowDB.newInstance(getApplicationContext()).loadTheme().size() == 0) {
            getThemeDataFromServer();
        } else {
            initGridLayout();
        }
    }
//专栏列表及其点击模式
    private void initGridLayout() {
        List<Theme> themes = KnowDB.newInstance(getApplicationContext()).loadTheme();
        for (Theme theme : themes) {
            final SlidingMenuTheme slidingMenuTheme = new SlidingMenuTheme(getApplicationContext());
            TextView textView = (TextView) slidingMenuTheme.findViewById(R.id.slidingmune_themeName);
            textView.setText(theme.getName());
            gridLayout_slidingMenu.addView(slidingMenuTheme);
            slidingMenuTheme.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Theme themeClick=KnowDB.newInstance(getApplicationContext()).loadTheme().get(gridLayout_slidingMenu.indexOfChild(slidingMenuTheme)-1);
                     themeFragment= new ThemeFragment(themeClick);
                    FragmentTransaction transaction_theme = manager.beginTransaction();
                    transaction_theme.replace(R.id.fragment_main, themeFragment);
                    transaction_theme.commit();
                    textView_mainTitle.setText(themeClick.getName());
                    slidingMenu.toggle();
                }
            });

        }
    }

    private void getThemeDataFromServer() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "http://news-at.zhihu.com/api/4/themes",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.contains("{\"limit\":1000,\"subscribed\":[],\"others\"")) {
                            ThemeAllBean themeAllBean = JSON.parseObject(response, ThemeAllBean.class);
                            KnowDB.newInstance(getApplicationContext()).deleteTheme();
                            for (Theme theme : themeAllBean.getOthers()) {
                                KnowDB.newInstance(getApplicationContext()).saveTheme(theme);
                            }
                            getSlidingThemeData();
                        }else {
                            Toast.makeText(MainActivity.this, "网络不可用", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, "网络不可用", Toast.LENGTH_SHORT).show();
                    }
                });
        VolleyUtils.newInstance(getApplicationContext()).addQueue(stringRequest);
    }

    class UpdateSlidingMenuThemReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            getSlidingThemeData();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(updateSlidingMenuThemReceiver);
    }
}
