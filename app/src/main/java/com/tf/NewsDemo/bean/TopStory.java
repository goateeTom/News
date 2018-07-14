package com.tf.NewsDemo.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class TopStory  implements Parcelable {
    private String ga_prefix;
    private String id;
    private String image;
    private String title;
    private int type;

    public TopStory() {
    }

    protected TopStory(Parcel in) {
        ga_prefix = in.readString();
        id = in.readString();
        image = in.readString();
        title = in.readString();
        type = in.readInt();
    }

    public static final Creator<TopStory> CREATOR = new Creator<TopStory>() {
        @Override
        public TopStory createFromParcel(Parcel in) {
            return new TopStory(in);
        }

        @Override
        public TopStory[] newArray(int size) {
            return new TopStory[size];
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(ga_prefix);
        dest.writeString(id);
        dest.writeString(image);
        dest.writeString(title);
        dest.writeInt(type);
    }
}
