package com.tf.NewsDemo.bean;

import android.os.Parcel;
import android.os.Parcelable;


public class ListStory implements Parcelable{
    private String ga_prefix;
    private String id;
    private String images;
    private String multipic;
    private String title;
    private int type;
    private String date;

    public ListStory() {
    }

    protected ListStory(Parcel in) {
        ga_prefix = in.readString();
        id = in.readString();
        images = in.readString();
        multipic = in.readString();
        title = in.readString();
        type = in.readInt();
        date = in.readString();
    }

    public static final Creator<ListStory> CREATOR = new Creator<ListStory>() {
        @Override
        public ListStory createFromParcel(Parcel in) {
            return new ListStory(in);
        }

        @Override
        public ListStory[] newArray(int size) {
            return new ListStory[size];
        }
    };

    public String getGa_prefix() {
        return ga_prefix;
    }

    public void setGa_prefix(String ga_prefix) {
        this.ga_prefix = ga_prefix;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public String getMultipic() {
        return multipic;
    }

    public void setMultipic(String multipic) {
        this.multipic = multipic;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(ga_prefix);
        dest.writeString(id);
        dest.writeString(images);
        dest.writeString(multipic);
        dest.writeString(title);
        dest.writeInt(type);
        dest.writeString(date);
    }
}
