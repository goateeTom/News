package com.tf.NewsDemo.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;


public class MyViewPagerAdapter extends PagerAdapter {
    private List<ImageView> imageViewList;
    private Context context;

    public MyViewPagerAdapter(List<ImageView> imageViewList, Context context) {
        this.imageViewList = imageViewList;
        this.context = context;
    }

    @Override
    public Object instantiateItem(final ViewGroup container, int position) {
        container.addView(imageViewList.get(position));
        return imageViewList.get(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(imageViewList.get(position));
    }

    @Override
    public int getCount() {
        return imageViewList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }
}
