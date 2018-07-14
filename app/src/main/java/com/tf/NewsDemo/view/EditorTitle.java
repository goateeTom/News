package com.tf.NewsDemo.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.tf.NewsDemo.R;

/**
 * Created by Wang on 2016/5/6.
 */
public class EditorTitle extends LinearLayout {
    public EditorTitle(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.layout_editortitle,this);
    }
}
