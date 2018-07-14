package com.tf.NewsDemo.bean;


public class Comment {
    private String author;
    private String avatar;
    private String content;
    private String id;
    private String likes;
    private long time;

    public Comment() {
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLikes() {
        return likes;
    }

    public void setLikes(String likes) {
        this.likes = likes;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
