package com.example.firstnationconnect;

public class User {
    private String firstName, lastName, email, username, profilePic;
    private int age;

    public User() {

    }

    public User(String firstName, String lastName, String email, String username, String profilePic, int age){
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.username = username;
        this.profilePic = profilePic;
        this.age = age;
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

    public String getProfilePic() { return username; }

    public void setProfilePic(String profilePic) { this.username = username; }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
