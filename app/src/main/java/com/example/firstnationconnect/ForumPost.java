package com.example.firstnationconnect;

import java.util.Date;

public class ForumPost extends Topic {

    private String postID;
    private String postName;
    private String postContent;
    private String postUser;
    private Date postDate;
    //private String image;


    public ForumPost(String postID, String topicName, String postName, String postContent, String postUser, Date postDate) {
        super(topicName);
        this.postID = postID;
        this.postName = postName;
        this.postContent = postContent;
        this.postUser = postUser;
        this.postDate = postDate;
    }

    public ForumPost() {

    }

    public String getPostID() {
        return postID;
    }

    public void setPostID(String postID) {
        this.postID = postID;
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
