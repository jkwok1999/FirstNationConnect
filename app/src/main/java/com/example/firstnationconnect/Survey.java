package com.example.firstnationconnect;

public class Survey {

    private String surveyUser;
    private String userID;
    private String surveyResponseOne;
    private String surveyResponseTwo;
    private String thoughts;


    public Survey(String surveyUser, String userID, String surveyResponseOne, String surveyResponseTwo, String thoughts) {
        this.surveyUser = surveyUser;
        this.userID = userID;
        this.surveyResponseOne = surveyResponseOne;
        this.surveyResponseTwo = surveyResponseTwo;
        this.thoughts = thoughts;
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

}
