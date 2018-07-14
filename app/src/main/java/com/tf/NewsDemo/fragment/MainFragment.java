package com.tf.NewsDemo.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.tf.NewsDemo.R;
import com.tf.NewsDemo.activity.NewsActivity;
import com.tf.NewsDemo.adapter.MyViewPagerAdapter;
import com.tf.NewsDemo.bean.AllArticles;
import com.tf.NewsDemo.bean.ListStory;
import com.tf.NewsDemo.bean.TopStory;
import com.tf.NewsDemo.db.KnowDB;
import com.tf.NewsDemo.utils.BitmapCache;
import com.tf.NewsDemo.utils.VolleyUtils;
import com.tf.NewsDemo.view.ItemView;

import java.util.ArrayList;
import java.util.List;


public class MainFragment extends Fragment implements ViewPager.OnPageChangeListener {
    private ViewPager viewPager;
    private TextView viewPager_title;
    private LinearLayout viewPager_dot;
    private View view;
    private AllArticles allArticles;
    private List<ImageView> imageViewList;
    private List<ImageView> imageViewDotList;
    private MyViewPagerAdapter myViewPagerAdapter;
    private List<TopStory> topStoriesList;
    private List<ListStory> listStoriesList;
    private GridLayout gridLayout;
    private boolean isFirst = true;
    private PullToRefreshScrollView pullToRefreshScrollView;
    private int count = 0;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_main, null);
        init();
        getArticles();
        initPullToRefreshScrollView();
        return view;
    }
    private void init() {
        viewPager = (ViewPager) view.findViewById(R.id.viewPager);
        viewPager_title = (TextView) view.findViewById(R.id.viewPager_title);
        viewPager_dot = (LinearLayout) view.findViewById(R.id.viewPager_dot);
        gridLayout = (GridLayout) view.findViewById(R.id.gridLayout);
        pullToRefreshScrollView = (PullToRefreshScrollView) view.findViewById(R.id.pullToRefreshScrollView);
        myHandler.sendEmptyMessageDelayed(1, 4000);
    }
    /**
     * 先查询数据库，看是否已经保存过今天的新闻内容，如果没查询到就从网上获取同时保存到本地的数据库中
     */
    private void getArticles() {
        if (KnowDB.newInstance(getContext()).loadTopStroy().size() == 0) {
            getDataFromServer();
        } else {
            initViewPager();
            initGridView(true);
        }
    }
//刷新功能
    private void initPullToRefreshScrollView() {
        pullToRefreshScrollView.setMode(PullToRefreshBase.Mode.BOTH);
        pullToRefreshScrollView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                //设置刷新标签
                pullToRefreshScrollView.getLoadingLayoutProxy().setRefreshingLabel("正在刷新");
                //设置释放标签
                pullToRefreshScrollView.getLoadingLayoutProxy().setReleaseLabel("释放立即刷新");

                //此处执行下拉刷新的逻辑处理
                getDataFromServer();
                pullToRefreshScrollView.onRefreshComplete();
                if (KnowDB.newInstance(getContext()).loadTheme().size() == 0){
                    Intent intent=new Intent();
                    intent.setAction("UpdateSlidingMenuThem");
                    getContext().sendBroadcast(intent);
                }
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                //设置刷新标签
                pullToRefreshScrollView.getLoadingLayoutProxy().setRefreshingLabel("正在加载");
                //设置释放标签
                pullToRefreshScrollView.getLoadingLayoutProxy().setReleaseLabel("加载更多");

                //此处执行上拉加载的逻辑处理
                count++;
                getListDataFromServer();
                pullToRefreshScrollView.onRefreshComplete();
            }
        });
    }

    /**
     * 轮播图功能
     * 初始化ViewPager的各种属性等，以及他的各种数据显示，适配器等
     */
    private void initViewPager() {
        imageViewList = new ArrayList<>();
        topStoriesList = KnowDB.newInstance(getContext()).loadTopStroy();
        for (int i = 0; i < topStoriesList.size(); i++) {
            ImageView imageView = new ImageView(getContext());
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            ImageLoader.ImageListener listener = ImageLoader.getImageListener(imageView, R.mipmap.ic_launcher, R.mipmap.ic_launcher);
            ImageLoader imageLoader = new ImageLoader(VolleyUtils.newInstance(getContext()).getRequestQueue(), new BitmapCache());
            imageLoader.get(topStoriesList.get(i).getImage(), listener);
            imageViewList.add(imageView);
        }
        myViewPagerAdapter = new MyViewPagerAdapter(imageViewList, getContext());
        viewPager.setAdapter(myViewPagerAdapter);
        viewPager_title.setText(topStoriesList.get(viewPager.getCurrentItem()).getTitle());
        viewPager.addOnPageChangeListener(this);//给viewpage添加一个手势滑动的监听器
        getDotImage();
    }
    private void getDotImage() {
        imageViewDotList = new ArrayList<>();
        viewPager_dot.removeAllViews();
        for (int i = 0; i < topStoriesList.size(); i++) {
            ImageView imageView = new ImageView(getContext());
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageViewDotList.add(imageView);
            //这是将这些小点，添加到相应的布局中，同时设置布局的各种属性，边距，大小等等。
            LinearLayout.LayoutParams layout = new LinearLayout.LayoutParams(20, 20);
            layout.leftMargin = 5;
            layout.rightMargin = 5;
            viewPager_dot.addView(imageViewDotList.get(i), layout);
        }
    }

    //根据ViewPager的位置，动态的更改小点的显示，使其和viewpager的位置对应一致
    public void getDots(int position) {
        for (int i = 0; i < imageViewDotList.size(); i++) {
            if (i == position) {
                imageViewDotList.get(i).setImageResource(R.mipmap.dot_yes);
            } else {
                imageViewDotList.get(i).setImageResource(R.mipmap.dot_no);
            }
        }
    }

    public void initGridView(boolean isUpdate) {
        if (isUpdate) {
            listStoriesList = KnowDB.newInstance(getContext()).loadListStroy();
            count = 0;
        } else {
            listStoriesList = allArticles.getStories();
        }

        TextView textView = new TextView(getContext());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 100);
        textView.setGravity(Gravity.CENTER_VERTICAL);
        textView.setPadding(20, 0, 0, 0);
        if (isUpdate) {
            gridLayout.removeAllViews();
            isFirst = true;
        }
        if (isFirst) {
            textView.setText("今日热闻");
            isFirst = false;
        } else {
            int date = Integer.parseInt(KnowDB.newInstance(getContext()).loadListStroy().get(0).getDate()) - 1 - count++;
            textView.setText(date + "");
        }
        gridLayout.addView(textView, params);
        for (final ListStory listStory : listStoriesList) {
            ItemView itemView = new ItemView(getContext());
            TextView textView_title = (TextView) itemView.findViewById(R.id.textView_item_stories_title);
            ImageView imageView_pic = (ImageView) itemView.findViewById(R.id.textView_item_stories_pic);
            textView_title.setText(listStory.getTitle());
            ImageLoader.ImageListener listener = ImageLoader.getImageListener(imageView_pic, R.mipmap.ic_launcher, R.mipmap.ic_launcher);
            ImageLoader imageLoader = new ImageLoader(VolleyUtils.newInstance(getContext()).getRequestQueue(), new BitmapCache());
            imageLoader.get(listStory.getImages().replace("[\"", "").replace("\"]", ""), listener);
            gridLayout.addView(itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(), NewsActivity.class);
                    intent.putExtra("story", listStory);
                    intent.putExtra("isTop", false);
                    getContext().startActivity(intent);
                }
            });

        }

    }







    private void getListDataFromServer() {
        int date = Integer.parseInt(KnowDB.newInstance(getContext()).loadListStroy().get(0).getDate()) - count;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "http://news.at.zhihu.com/api/4/news/before/" + date,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        allArticles = JSON.parseObject(response, AllArticles.class);
                        initGridView(false);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), "网络不可用", Toast.LENGTH_SHORT).show();
                    }
                });
        VolleyUtils.newInstance(getContext()).addQueue(stringRequest);
    }


    private void getDataFromServer() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "http://news-at.zhihu.com/api/4/news/latest",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (!response.contains("stories")) {
                            Toast.makeText(getContext(), "网络不可用", Toast.LENGTH_SHORT).show();
                        } else {
                            allArticles = JSON.parseObject(response, AllArticles.class);
                            KnowDB.newInstance(getContext()).deleteListStory();
                            KnowDB.newInstance(getContext()).deleteTopStory();
                            for (TopStory topStory : allArticles.getTop_stories()) {
                                KnowDB.newInstance(getContext()).saveTopStroy(topStory);
                            }
                            for (ListStory listStory : allArticles.getStories()) {
                                KnowDB.newInstance(getContext()).saveListStroy(listStory, allArticles.getDate());
                            }
                            getArticles();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), "网络不可用", Toast.LENGTH_SHORT).show();
                    }
                });
        VolleyUtils.newInstance(getContext()).addQueue(stringRequest);
    }





    @Override
    public void onPageScrolled(final int position, float positionOffset, int positionOffsetPixels) {
        viewPager_title.setText(topStoriesList.get(position).getTitle());
        getDots(position);
        imageViewList.get(position).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), NewsActivity.class);
                intent.putExtra("story", topStoriesList.get(position));
                intent.putExtra("isTop", true);
                getContext().startActivity(intent);
            }
        });
    }

    @Override
    public void onPageSelected(int position) {
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    private Handler myHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            myHandler.sendEmptyMessageDelayed(1, 4000);
            if (topStoriesList != null) {
                if (viewPager.getCurrentItem() == topStoriesList.size() - 1) {
                                                                                                                               (0, false);
                } else {
                    viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
                }
            }
        }
    };
}
