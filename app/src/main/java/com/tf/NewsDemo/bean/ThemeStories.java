package com.tf.NewsDemo.bean;

import java.util.List;


public class ThemeStories {
    private List<ThemeStory> stories;
    private List<ZhubianBean> editors;

    public List<ThemeStory> getStories() {
        return stories;
    }

    public void setStories(List<ThemeStory> stories) {
        this.stories = stories;
    }

    public List<ZhubianBean> getEditors() {
        return editors;
    }

    public void setEditors(List<ZhubianBean> editors) {
        this.editors = editors;
    }
}
