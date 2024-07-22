package com.example.myapplication.movieapp.model.remote;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiPoint {
    @GET("/3/movie/top_rated?language=en-US&api_key=71426de873c8d6cc281dceba7b73d976")
    Call<Result> getTopRatedMovies();

    @GET("/3/movie/now_playing?language=en-US&api_key=71426de873c8d6cc281dceba7b73d976")
    Call<Result> getNowPlayingMovies();

    @GET("/3/movie/{movie_id}?language=en-US&api_key=71426de873c8d6cc281dceba7b73d976")
    Call<Movie> findMovieByID(@Path("movie_id") int movie_id);

    @GET("/3/search/movie?language=en-US&api_key=71426de873c8d6cc281dceba7b73d976")
    Call<Result> searchMovie(@Query("query") String query);
}
