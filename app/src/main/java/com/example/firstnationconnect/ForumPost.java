package com.example.firstnationconnect;

import android.location.Location;

import com.google.firebase.firestore.GeoPoint;

import java.util.Date;

public class ForumPost {

    private String topicName;
    private String postID;
    private String postName;
    private String postContent;
    private String postUserID;
    private Date postDate;
    private String postUsername;
    private String lastReply;
    private GeoPoint postLocation;
    private String postType;
    private String postImage;
    private String postVideo;


    public ForumPost(String postID, String topicName, String postName, String postContent, String postUserID,
                     Date postDate, String postUsername, String lastReply, GeoPoint postLocation, String postType, String postImage, String postVideo) {
        this.topicName = topicName;
        this.postID = postID;
        this.postName = postName;
        this.postContent = postContent;
        this.postUserID = postUserID;
        this.postDate = postDate;
        this.postUsername = postUsername;
        this.lastReply = lastReply;
        this.postLocation = postLocation;
        this.postType = postType;
        this.postImage = postImage;
        this.postVideo = postVideo;
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

    public String getPostUserID() {
        return postUserID;
    }

    public void setPostUserID(String postUser) {
        this.postUserID = postUser;
    }

    public Date getPostDate() {
        return postDate;
    }

    public void setPostDate(Date postDate) {
        this.postDate = postDate;
    }

    public String getPostUsername() {
        return postUsername;
    }

    public void setPostUsername(String userImage) {
        this.postUsername = userImage;
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

    public String getPostType() {
        return postType;
    }

    public void setPostType(String postType) {
        this.postType = postType;
    }

    public String getPostImage() {
        return postImage;
    }

    public void setPostImage(String postImage) {
        this.postImage = postImage;
    }

    public String getPostVideo() {
        return postVideo;
    }

    public void setPostVideo(String postVideo) {
        this.postVideo = postVideo;
    }
}
