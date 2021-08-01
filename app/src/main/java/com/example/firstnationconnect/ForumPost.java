package com.example.firstnationconnect;

import android.location.Location;

import com.google.firebase.firestore.GeoPoint;

import java.util.Date;

public class ForumPost {

    private String topicName;
    private String postID;
    private String postName;
    private String postContent;
    private String postUser;
    private Date postDate;
    private String image;
    private String lastReply;
    private GeoPoint postLocation;


    public ForumPost(String postID, String topicName, String postName, String postContent, String postUser, Date postDate, String image, String lastReply, GeoPoint postLocation) {
        this.topicName = topicName;
        this.postID = postID;
        this.postName = postName;
        this.postContent = postContent;
        this.postUser = postUser;
        this.postDate = postDate;
        this.image = image;
        this.lastReply = lastReply;
        this.postLocation = postLocation;
    }

    public ForumPost() {

    }

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getLastReply() {
        return lastReply;
    }

    public void setLastReply(String lastReply) {
        this.lastReply = lastReply;
    }

    public GeoPoint getPostLocation() {
        return postLocation;
    }

    public void setPostLocation(GeoPoint postLocation) {
        this.postLocation = postLocation;
    }
}
