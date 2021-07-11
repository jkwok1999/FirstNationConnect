package com.example.firstnationconnect;

import java.util.Date;

public class ForumPost {

    private String postName;
    private String postContent;
    private String postUser;
    private Date postDate;

    public ForumPost(String postName, String postContent, String postUser, Date postDate) {
        this.postName = postName;
        this.postContent = postContent;
        this.postUser = postUser;
        this.postDate = postDate;
    }

    public ForumPost() {

    }

    public String getPostName() {
        return postName;
    }

    public void setPostName(String postName) {
        this.postName = postName;
    }

    public String getPostContent() {
        return postContent;
    }

    public void setPostContent(String postContent) {
        this.postContent = postContent;
    }

    public String getPostUser() {
        return postUser;
    }

    public void setPostUser(String postUser) {
        this.postUser = postUser;
    }

    public Date getPostDate() {
        return postDate;
    }

    public void setPostDate(Date postDate) {
        this.postDate = postDate;
    }
}