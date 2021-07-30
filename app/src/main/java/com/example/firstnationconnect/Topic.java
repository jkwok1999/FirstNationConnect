package com.example.firstnationconnect;

import java.util.ArrayList;
import java.util.List;

public class Topic {
    private String topicName;
    private String lastPost;


    public Topic(String topicName, String lastPost) {
        this.topicName = topicName;
        this.lastPost = lastPost;
    }

    public Topic() {

    }

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public String getLastPost() {
        return lastPost;
    }

    public void setLastPost(String lastPost) {
        this.lastPost = lastPost;
    }
}


