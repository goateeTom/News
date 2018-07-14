package com.tf.NewsDemo.bean;

import java.util.List;


public class AllArticles {
    private String date;
    private List<ListStory> stories;
    private List<TopStory> top_stories;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<ListStory> getStories() {
        return stories;
    }

    public void setStories(List<ListStory> stories) {
        this.stories = stories;
    }

    public List<TopStory> getTop_stories() {
        return top_stories;
    }

    public void setTop_stories(List<TopStory> top_stories) {
        this.top_stories = top_stories;
    }
}
