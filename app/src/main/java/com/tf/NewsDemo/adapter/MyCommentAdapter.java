package com.tf.NewsDemo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.tf.NewsDemo.R;
import com.tf.NewsDemo.bean.Comment;
import com.tf.NewsDemo.utils.BitmapCache;
import com.tf.NewsDemo.utils.VolleyUtils;

import java.util.List;


public class MyCommentAdapter extends BaseAdapter {
    private Context context;
    private List<Comment> commentList;

    public MyCommentAdapter(Context context, List<Comment> commentList) {
        this.context = context;
        this.commentList = commentList;
    }

    @Override
    public int getCount() {
        return commentList.size();
    }

    @Override
    public Object getItem(int position) {
        return commentList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_comment, null);
            viewHolder = new ViewHolder();
            viewHolder.imageView_commentPic = (ImageView) convertView.findViewById(R.id.comment_pic);
            viewHolder.textView_commentName = (TextView) convertView.findViewById(R.id.comment_name);
            viewHolder.textView_commentContent = (TextView) convertView.findViewById(R.id.comment_content);
            viewHolder.textView_commentZanNum = (TextView) convertView.findViewById(R.id.zan_comment_num);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Comment comment = commentList.get(position);
        ImageLoader.ImageListener listener = ImageLoader.getImageListener(viewHolder.imageView_commentPic, R.mipmap.ic_launcher, R.mipmap.ic_launcher);
        //使用了缓存的工具类
        ImageLoader imageLoader = new ImageLoader(VolleyUtils.newInstance(context).getRequestQueue(), new BitmapCache());
        imageLoader.get(comment.getAvatar(), listener);
        viewHolder.textView_commentName.setText(comment.getAuthor());
        if (comment.getContent().length() > 70) {
            viewHolder.textView_commentContent.setText(comment.getContent().substring(0, 70) + "......(点击查看全部)");
        } else {
            viewHolder.textView_commentContent.setText(comment.getContent());
        }
        viewHolder.textView_commentZanNum.setText(comment.getLikes());
        return convertView;
    }

    class ViewHolder {
        ImageView imageView_commentPic;
        TextView textView_commentName, textView_commentContent,  textView_commentZanNum;
    }

}
