package com.example.myapplication.movieapp.model.firebase;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class User implements Serializable {
    private String id;
    private String fullName;
    private String login;
    private String password;
    private String email;
    private String photo;
    private List<Integer> favouriteMovies = new ArrayList<>();

    public User(){}

    public User(String login, String fullName, String email, String password, String photo) {
        this.fullName = fullName;
        this.login = login;
        this.password = password;
        this.email = email;
        this.photo = photo;
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

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public List<Integer> getFavouriteMovies() {
        return favouriteMovies;
    }

}
