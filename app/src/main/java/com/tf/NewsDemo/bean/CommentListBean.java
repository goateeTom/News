package com.tf.NewsDemo.bean;

import java.util.List;


public class CommentListBean {
    private List<Comment> comments;

    public CommentListBean() {
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }
}
