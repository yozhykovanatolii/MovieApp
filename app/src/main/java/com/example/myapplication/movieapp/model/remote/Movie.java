package com.example.myapplication.movieapp.model.remote;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class Movie implements Serializable {
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

    public String getTitleMovie() {
        return titleMovie;
    }

    public String getDescription() {
        return description;
    }

    public String getPosterPhoto() {
        return posterPhoto;
    }

    public String getRelease() {
        return release;
    }

    public double getReview_average() {
        return review_average;
    }
}
