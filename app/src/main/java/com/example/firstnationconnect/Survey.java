package com.example.firstnationconnect;

import com.google.firebase.firestore.GeoPoint;

import java.util.Date;

public class Survey {

    private String surveyUser;
    private String userID;
    private String surveyResponseOne;
    private String surveyResponseTwo;
    private String thoughts;
    private Date surveyDate;
    private GeoPoint surveyLocation;

    private String surveyResponseThree;


    public Survey(String surveyUser, String userID, String surveyResponseOne, String surveyResponseTwo, String thoughts, Date surveyDate, GeoPoint surveyLocation, String surveyResponseThree) {
        this.surveyUser = surveyUser;
        this.userID = userID;
        this.surveyResponseOne = surveyResponseOne;
        this.surveyResponseTwo = surveyResponseTwo;
        this.thoughts = thoughts;
        this.surveyDate = surveyDate;
        this.surveyLocation = surveyLocation;
        this.surveyResponseThree = surveyResponseThree;
    }

    public Survey() {

    }


    public String getSurveyUser() {
        return surveyUser;
    }

    public void setSurveyUser(String surveyUser) {
        this.surveyUser = surveyUser;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getSurveyResponseOne() {
        return surveyResponseOne;
    }

    public void setSurveyResponseOne(String surveyResponseOne) {
        this.surveyResponseOne = surveyResponseOne;
    }

    public String getSurveyResponseTwo() {
        return surveyResponseTwo;
    }

    public void setSurveyResponseTwo(String surveyResponseTwo) {
        this.surveyResponseOne = surveyResponseOne;
    }

    public String getThoughts() {
        return thoughts;
    }

    public void setThoughts(String thoughts) {
        this.thoughts = thoughts;
    }

    public Date getSurveyDate() {
        return surveyDate;
    }

    public void setSurveyDate(Date surveyDate) {
        this.surveyDate = surveyDate;
    }

    public GeoPoint getSurveyLocation() {
        return surveyLocation;
    }

    public void setSurveyLocation(GeoPoint surveyLocation) {
        this.surveyLocation = surveyLocation;
    }

    public String getSurveyResponseThree() {
        return surveyResponseThree;
    }

    public void setSurveyResponseThree(String surveyResponseThree) {
        this.surveyResponseThree = surveyResponseThree;
    }
}
