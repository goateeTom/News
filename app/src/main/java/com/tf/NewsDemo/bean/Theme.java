package com.tf.NewsDemo.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class Theme implements Parcelable{
    private String name;
    private String id;
    private String thumbnail;//图片的网址
    private String description;

    public Theme() {
    }

    protected Theme(Parcel in) {
        name = in.readString();
        id = in.readString();
        thumbnail = in.readString();
        description = in.readString();
    }

    public static final Creator<Theme> CREATOR = new Creator<Theme>() {
        @Override
        public Theme createFromParcel(Parcel in) {
            return new Theme(in);
        }

        @Override
        public Theme[] newArray(int size) {
            return new Theme[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(id);
        dest.writeString(thumbnail);
        dest.writeString(description);
    }
}
