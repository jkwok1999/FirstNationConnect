package com.example.firstnationconnect;

public class Survey {

    private String surveyUser;
    private String userID;
    private String surveyResponseOne;

    public Survey(String surveyUser, String userID, String surveyResponseOne) {
        this.surveyUser = surveyUser;
        this.userID = userID;
        this.surveyResponseOne = surveyResponseOne;
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

}
