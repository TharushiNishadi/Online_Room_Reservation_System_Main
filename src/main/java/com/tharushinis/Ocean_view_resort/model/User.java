package com.tharushinis.Ocean_view_resort.model;


public class User extends BaseModel {
    public String username;
    public String password;
    public String fullName;
    private String email;


    public User() {   // ---> is this Constructors
        super();
    }

    public User(String username, String password, String fullName,String email) {
        super();
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.email = email;

    }

    @Override   // use in Polymorphism
    public String getDisplayName() {  // --> Implementation of abstract method
        return fullName;
    }


    // < ----  Getters and setters ---- >


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", fullName='" + fullName + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
