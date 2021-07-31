package com.example.firstnationconnect;

public class User {
    private String firstName, lastName, email, username, gender, profilePic, firstNationDescent;
    private int age;

    public User() {

    }


    public User(String firstName, String lastName, String email, String username, int age, String gender, String profilePic, String firstNationDescent
    ) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.username = username;
        this.age = age;
        this.gender = gender;
        this.profilePic = profilePic;
        this.firstNationDescent = firstNationDescent;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public String getFirstNationDescent() {
        return firstNationDescent;
    }

    public void setFirstNationDescent(String firstNationDescent) {
        this.firstNationDescent = firstNationDescent;
    }
}
