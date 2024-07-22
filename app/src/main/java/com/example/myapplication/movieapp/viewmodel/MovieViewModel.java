package com.example.myapplication.movieapp.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.myapplication.movieapp.model.remote.Movie;
import com.example.myapplication.movieapp.model.repository.MovieRepository;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class MovieViewModel extends ViewModel {
    private MovieRepository movieRepository;

    @Inject
    public MovieViewModel(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    public LiveData<List<Movie>> getTopRatedFilms(){
        return movieRepository.getTopRatedFilmsFromApi();
    }

    public LiveData<List<Movie>> getNowPlayingFilms(){
        return movieRepository.getNowPlayingFilmsFromApi();
    }

    public LiveData<Movie> findFilmById(int film_id){
        return movieRepository.findFilmById(film_id);
    }

    public LiveData<List<Movie>> searchFilm(String query){
        return movieRepository.searchFilms(query);
    }
}
