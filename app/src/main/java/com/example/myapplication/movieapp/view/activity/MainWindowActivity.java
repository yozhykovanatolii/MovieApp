package com.example.myapplication.movieapp.view.activity;


import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.myapplication.movieapp.R;
import com.example.myapplication.movieapp.viewmodel.MovieViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MainWindowActivity extends AppCompatActivity {
    private MovieViewModel movieViewModel;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_window);
        button = findViewById(R.id.button2);
        movieViewModel = new ViewModelProvider(this).get(MovieViewModel.class);
        //getUser();
        isMovieInFavourite();
    }

    private void getFavouriteMovies(){
    }

    private void checkMovieInFavourites(List<Integer> movies){
        if(!movies.contains(375)){
            movies.add(375);
            button.setBackgroundColor(Color.parseColor("#1877F2"));
        }else{
            movies.remove(Integer.valueOf(375));
            button.setBackgroundColor(Color.parseColor("#FF0000"));
        }
        movieViewModel.updateFavouriteFilms(movies);
    }

    public void addToFavourite(View view){
        movieViewModel.getFavouriteFilms().observe(this, movies -> {
            if(movies != null){
                checkMovieInFavourites(movies);
            }
        });
    }

    private void isMovieInFavourite(){
        movieViewModel.getFavouriteFilms().observe(this, movies -> {
            if(movies != null && movies.contains(375)){
                button.setBackgroundColor(Color.parseColor("#1877F2"));
            }
        });
    }
}