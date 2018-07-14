package com.tf.NewsDemo.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.tf.NewsDemo.bean.ListStory;
import com.tf.NewsDemo.bean.Theme;
import com.tf.NewsDemo.bean.TopStory;

import java.util.ArrayList;
import java.util.List;


public class KnowDB {
    //数据库名称
    public static final String DB_NAME = "myknow";
    //数据库版本
    public static final int VERSION = 1;

    public static KnowDB knowDB;
    public SQLiteDatabase db;

    /**
     * 将构造方法私有化
     */
    private KnowDB(Context context) {
        KnowOpenHelper dbHelper = new KnowOpenHelper(context,
                DB_NAME, null, VERSION);
        db = dbHelper.getWritableDatabase();
    }

    /**
     * 获取DB的实例。
     */
    public synchronized static KnowDB newInstance(Context context) {
        if (knowDB == null) {
            knowDB = new KnowDB(context);
        }
        return knowDB;
    }

    /**
     * 将ListStroy实例存储到数据库。
     */
    public void saveListStroy(ListStory listStory,String date) {
        if (listStory != null) {
            ContentValues values = new ContentValues();
            values.put("ga_prefix", listStory.getGa_prefix());
            values.put("id", listStory.getId());
            values.put("images", listStory.getImages());
            values.put("multipic", listStory.getMultipic());
            values.put("title", listStory.getTitle());
            values.put("type", listStory.getType());
            values.put("date",  date);
            db.insert("stories", null, values);
        }
    }

    /**
     * 将TopStroy实例存储到数据库。
     */
    public void saveTopStroy(TopStory topStory) {
        if (topStory != null) {
            ContentValues values = new ContentValues();
            values.put("ga_prefix", topStory.getGa_prefix());
            values.put("id", topStory.getId());
            values.put("image", topStory.getImage());
            values.put("title", topStory.getTitle());
            values.put("type", topStory.getType());
            db.insert("top_stories", null, values);
        }
    }

    /**
     * 将Theme实例存储到数据库。
     */
    public void saveTheme(Theme theme) {
        if (theme != null) {
            ContentValues values = new ContentValues();
            values.put("name", theme.getName());
            values.put("id", theme.getId());
            values.put("description", theme.getDescription());
            values.put("thumbnail", theme.getThumbnail());
            db.insert("theme", null, values);
        }
    }

    /**
     * 从数据库读取theme信息
     */
    public List<Theme> loadTheme(){
        List<Theme> themes = new ArrayList<Theme>();
        Cursor cursor = db
                .query("theme", null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                Theme theme = new Theme();
                theme.setName(cursor.getString(cursor.getColumnIndex("name")));
                theme.setId(cursor.getString(cursor.getColumnIndex("id")));
                theme.setThumbnail(cursor.getString(cursor.getColumnIndex("thumbnail")));
                theme.setDescription(cursor.getString(cursor.getColumnIndex("description")));
                themes.add(theme);
            } while (cursor.moveToNext());
        }
        return themes;
    }

    /**
     * 从数据库读取stories信息。
     */
    public List<ListStory> loadListStroy() {
        List<ListStory> list = new ArrayList<ListStory>();
        Cursor cursor = db
                .query("stories", null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                ListStory listStory = new ListStory();
                listStory.setGa_prefix(cursor.getString(cursor.getColumnIndex("ga_prefix")));
                listStory.setId(cursor.getString(cursor.getColumnIndex("id")));
                listStory.setImages(cursor.getString(cursor.getColumnIndex("images")));
                listStory.setMultipic(cursor.getString(cursor.getColumnIndex("multipic")));
                listStory.setTitle(cursor.getString(cursor.getColumnIndex("title")));
                listStory.setType(cursor.getInt(cursor.getColumnIndex("type")));
                listStory.setDate(cursor.getString(cursor.getColumnIndex("date")));
                list.add(listStory);
            } while (cursor.moveToNext());
        }
        return list;
    }

    /**
     * 从数据库读取TopStroy信息。
     */
    public List<TopStory> loadTopStroy() {
        List<TopStory> list = new ArrayList<TopStory>();
        Cursor cursor = db
                .query("top_stories", null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                TopStory topStory = new TopStory();
                topStory.setGa_prefix(cursor.getString(cursor.getColumnIndex("ga_prefix")));
                topStory.setId(cursor.getString(cursor.getColumnIndex("id")));
                topStory.setImage(cursor.getString(cursor.getColumnIndex("image")));
                topStory.setTitle(cursor.getString(cursor.getColumnIndex("title")));
                topStory.setType(cursor.getInt(cursor.getColumnIndex("type")));
                list.add(topStory);
            } while (cursor.moveToNext());
        }
        return list;
    }

    /**
     * 用于更新stories时，先删除所有的原来的消息
     */
    public void deleteListStory() {
        db.delete("stories", null, null);
    }

    public void deleteTopStory() {
        db.delete("top_stories", null, null);
    }

    public void deleteTheme() {
        db.delete("theme", null, null);
    }

}
