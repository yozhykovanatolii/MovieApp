package com.example.myapplication.movieapp.model.remote;

import com.google.gson.annotations.SerializedName;


public class Movie {
    @SerializedName("id")
    private int idMovie;
    @SerializedName("title")
    private String titleMovie;
    @SerializedName("overview")
    private String description;
    @SerializedName("poster_path")
    private String posterPhoto;
    @SerializedName("release_date")
    private String release;
    @SerializedName("vote_average")
    private double review_average;

    public Movie(int idMovie, String titleMovie, String description, String posterPhoto, String release, double review_average) {
        this.idMovie = idMovie;
        this.titleMovie = titleMovie;
        this.description = description;
        this.posterPhoto = posterPhoto;
        this.release = release;
        this.review_average = review_average;
    }

    public int getIdMovie() {
        return idMovie;
    }

    public void setIdMovie(int idMovie) {
        this.idMovie = idMovie;
    }

    public String getTitleMovie() {
        return titleMovie;
    }

    public void setTitleMovie(String titleMovie) {
        this.titleMovie = titleMovie;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPosterPhoto() {
        return posterPhoto;
    }

    public void setPosterPhoto(String posterPhoto) {
        this.posterPhoto = posterPhoto;
    }

    public String getRelease() {
        return release;
    }

    public void setRelease(String release) {
        this.release = release;
    }

    public double getReview_average() {
        return review_average;
    }

    public void setReview_average(double review_average) {
        this.review_average = review_average;
    }
}
