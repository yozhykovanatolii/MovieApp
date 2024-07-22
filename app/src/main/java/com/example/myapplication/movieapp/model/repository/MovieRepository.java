package com.example.myapplication.movieapp.model.repository;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.myapplication.movieapp.model.remote.ApiPoint;
import com.example.myapplication.movieapp.model.remote.Movie;
import com.example.myapplication.movieapp.model.remote.Result;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieRepository {
    private FirebaseFirestore firestore;
    private FirebaseAuth firebaseAuth;
    private ApiPoint apiPoint;

    @Inject
    public MovieRepository(FirebaseFirestore firestore, FirebaseAuth firebaseAuth, ApiPoint apiPoint) {
        this.firestore = firestore;
        this.firebaseAuth = firebaseAuth;
        this.apiPoint = apiPoint;
    }

    public LiveData<List<Movie>> getTopRatedFilmsFromApi(){
        MutableLiveData<List<Movie>> topRatedFilms = new MutableLiveData<>();
        apiPoint.getTopRatedMovies().enqueue(new Callback<Result>() {
            @Override
            public void onResponse(@NonNull Call<Result> call, @NonNull Response<Result> response) {
                if(response.isSuccessful() && response.body() != null){
                    topRatedFilms.setValue(response.body().getMovies());
                }else{
                    topRatedFilms.setValue(null);
                }
            }

            @Override
            public void onFailure(@NonNull Call<Result> call, @NonNull Throwable t) {
                Log.i("TopRatedFilms", Objects.requireNonNull(t.getMessage()));
            }
        });
        return topRatedFilms;
    }

    public LiveData<List<Movie>> getNowPlayingFilmsFromApi(){
        MutableLiveData<List<Movie>> nowPlayingFilms = new MutableLiveData<>();
        apiPoint.getNowPlayingMovies().enqueue(new Callback<Result>() {
            @Override
            public void onResponse(@NonNull Call<Result> call, @NonNull Response<Result> response) {
                if(response.isSuccessful() && response.body() != null){
                    nowPlayingFilms.setValue(response.body().getMovies());
                }else{
                    nowPlayingFilms.setValue(null);
                }
            }

            @Override
            public void onFailure(@NonNull Call<Result> call, @NonNull Throwable t) {
                Log.i("NowPlayingFilms", Objects.requireNonNull(t.getMessage()));
            }
        });
        return nowPlayingFilms;
    }

    public LiveData<Movie> findFilmById(int film_id){
        MutableLiveData<Movie> movieMutableLiveData = new MutableLiveData<>();
        apiPoint.findMovieByID(film_id).enqueue(new Callback<Movie>() {
            @Override
            public void onResponse(@NonNull Call<Movie> call, @NonNull Response<Movie> response) {
                if(response.isSuccessful() && response.body() != null){
                    movieMutableLiveData.setValue(response.body());
                }else{
                    movieMutableLiveData.setValue(null);
                }
            }

            @Override
            public void onFailure(@NonNull Call<Movie> call, @NonNull Throwable t) {
                Log.i("FindFilmById", Objects.requireNonNull(t.getMessage()));
            }
        });
        return movieMutableLiveData;
    }

    public LiveData<List<Movie>> searchFilms(String query){
        MutableLiveData<List<Movie>> films = new MutableLiveData<>();
        apiPoint.searchMovie(query).enqueue(new Callback<Result>() {
            @Override
            public void onResponse(@NonNull Call<Result> call, @NonNull Response<Result> response) {
                if(response.isSuccessful() && !Objects.requireNonNull(response.body()).getMovies().isEmpty()){
                    films.setValue(response.body().getMovies());
                }else{
                    films.setValue(null);
                }
            }

            @Override
            public void onFailure(@NonNull Call<Result> call, @NonNull Throwable t) {
                Log.i("SearchFilm", Objects.requireNonNull(t.getMessage()));
            }
        });
        return films;
    }
}
