package com.example.firstnationconnect;

public class User {
    public String firstName, lastName, email, username;
    public int age;

    public User() {

    }
    public User(String firstName, String lastName, String email, String username, int age){
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.username = username;
        this.age = age;
    }

}
