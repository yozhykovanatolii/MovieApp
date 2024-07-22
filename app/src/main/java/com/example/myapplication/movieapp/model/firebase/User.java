package com.example.myapplication.movieapp.model.firebase;

import java.util.ArrayList;
import java.util.List;

public class User {
    private String id;
    private String fullName;
    private String login;
    private String password;
    private String email;
    private String phoneNumber;
    private String photo = "photo";
    private List<String> favouriteCars = new ArrayList<>();

    public User(){}

    public User(String fullName, String login, String password, String email, String phoneNumber) {
        this.fullName = fullName;
        this.login = login;
        this.password = password;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public List<String> getFavouriteCars() {
        return favouriteCars;
    }

    public void setFavouriteCars(List<String> favouriteCars) {
        this.favouriteCars = favouriteCars;
    }
}
