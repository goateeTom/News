package com.tf.NewsDemo.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.tf.NewsDemo.R;

/**
 * Created by Wang on 2016/5/6.
 */
public class SlidingMenuTheme extends LinearLayout {
    public SlidingMenuTheme(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.slidingmune_theme,this);
    }
}
