package com.example.myapplication.movieapp.view.activity;

import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.myapplication.movieapp.R;
import com.example.myapplication.movieapp.model.remote.Movie;
import com.example.myapplication.movieapp.viewmodel.MovieViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class FavouriteMoviesActivity extends AppCompatActivity {
    private MovieViewModel movieViewModel;
    private ArrayList<Movie> favouriteMovies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite_movies);
        movieViewModel = new ViewModelProvider(this).get(MovieViewModel.class);
        favouriteMovies = new ArrayList<>();
        getFavouriteMovies();
    }

    private void getFavouriteMovies() {
        movieViewModel.getFavouriteFilms().observe(this, movies -> {
            if (movies != null) {
                getMovieById(movies);
            }
        });
    }

    private void getMovieById(List<Integer> movies){
        for(Integer id: movies){
            addMovieIntoList(id, movies.size());
        }
    }

    private void addMovieIntoList(int movieId, int size) {
        movieViewModel.findFilmById(movieId).observe(this, movie -> {
            if (movie != null) {
                favouriteMovies.add(movie);
            }
            if(favouriteMovies.size() == size){
                printFavouriteMovies();
            }
        });
    }

    private void printFavouriteMovies() {
        for (Movie movie : favouriteMovies) {
            Toast.makeText(getApplicationContext(), "Movie ID: " + movie.getIdMovie(), Toast.LENGTH_LONG).show();
        }
    }


}