package com.tf.NewsDemo.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.tf.NewsDemo.R;

/**
 * Created by Wang on 2016/5/11.
 */
public class ItemView extends LinearLayout {

    public ItemView(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.item_stories,this);
    }

    public ItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.item_stories,this);
    }
}
