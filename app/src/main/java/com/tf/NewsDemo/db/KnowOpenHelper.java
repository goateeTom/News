package com.tf.NewsDemo.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class KnowOpenHelper extends SQLiteOpenHelper {

    private Context context;
    /**
     *  stories表建表语句
     */
    public static final String CREATE_STORIES = "create table stories ("
            + "id_first integer primary key autoincrement, "
            + "ga_prefix text, "
            + "id text, "
            + "images text, "
            + "multipic text, "
            + "title text, "
            + "type integer, "
            + "date text)";
    /**
     *  top_stories表建表语句
     */
    public static final String CREATE_TOP_STORIES = "create table top_stories ("
            + "id_first integer primary key autoincrement, "
            + "ga_prefix text, "
            + "id text, "
            + "image text, "
            + "title text, "
            + "type integer)";

    /**
     *  侧滑菜单的主题建表语句
     */
    public static final String CREATE_THEME = "create table theme ("
            + "id_first integer primary key autoincrement, "
            + "name text, "
            + "thumbnail text, "
            + "description text, "
            + "id text)";

    public KnowOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.context=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_STORIES);
        db.execSQL(CREATE_TOP_STORIES);
        db.execSQL(CREATE_THEME);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
