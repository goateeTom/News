package com.tf.NewsDemo.utils;

import android.content.Context;
import android.graphics.Bitmap;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;


public class VolleyUtils {
    private static VolleyUtils volleyUtils;
    private RequestQueue requestQueue;
    private Context context;
    private ImageLoader imageLoader;

    public VolleyUtils(Context context) {
        this.context = context;
        if (requestQueue==null){
            requestQueue= Volley.newRequestQueue(context);
        }
    }

    public static synchronized VolleyUtils newInstance(Context context){
        if (volleyUtils==null){
            volleyUtils=new VolleyUtils(context);
        }
        return volleyUtils;
    }

    public RequestQueue getRequestQueue(){
        return requestQueue;
    }

    public ImageLoader getImageLoader(){
        imageLoader=new ImageLoader(requestQueue, new ImageLoader.ImageCache() {
            @Override
            public Bitmap getBitmap(String url) {
                return null;
            }

            @Override
            public void putBitmap(String url, Bitmap bitmap) {

            }
        });
        return imageLoader;
    }


    public void addQueue(Request request){
        requestQueue.add(request);
    }


}
