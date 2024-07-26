package com.example.myapplication.movieapp.view.activity;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.myapplication.movieapp.R;
import com.example.myapplication.movieapp.model.remote.Movie;
import com.example.myapplication.movieapp.receiver.InternetReceiver;
import com.example.myapplication.movieapp.viewmodel.MovieViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.squareup.picasso.Picasso;

import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MovieDetailActivity extends AppCompatActivity {
    private MovieViewModel movieViewModel;
    private Movie movie;
    private BroadcastReceiver broadcastReceiver;
    private ShapeableImageView posterMovie;
    private TextView titleMovie, reviewAverage, releaseDate, descriptionText;
    private FloatingActionButton favouriteButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        movieViewModel = new ViewModelProvider(this).get(MovieViewModel.class);
        movie = (Movie) getIntent().getSerializableExtra("Movie");
        broadcastReceiver = new InternetReceiver();
        initComponents();
        initComponentsData();
        isMovieInFavourite();
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(broadcastReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(broadcastReceiver);
    }

    private void initComponents(){
        initTextsView();
        posterMovie = findViewById(R.id.posterMovie);
        favouriteButton = findViewById(R.id.favouriteButton);
    }

    private void initTextsView(){
        titleMovie = findViewById(R.id.titleMovie);
        reviewAverage = findViewById(R.id.reviewAverage);
        releaseDate = findViewById(R.id.releaseDate);
        descriptionText = findViewById(R.id.descriptionText);
    }

    private void initComponentsData(){
        Picasso.get().load("https://image.tmdb.org/t/p/w500" + movie.getPosterPhoto()).into(posterMovie);
        titleMovie.setText(movie.getTitleMovie());
        reviewAverage.setText(String.format("%.1f", movie.getReview_average()));
        releaseDate.setText(movie.getRelease());
        descriptionText.setText(movie.getDescription());
    }

    public void backToMainWindow(View view){
        Intent intent = new Intent(this, MainWindowActivity.class);
        startActivity(intent);
    }

    public void addToFavourite(View view){
        movieViewModel.getFavouriteFilms().observe(this, movies -> {
            if(movies != null){
                checkMovieInFavourites(movies);
            }
        });
    }

    private void checkMovieInFavourites(List<Integer> movies){
        if(!movies.contains(movie.getIdMovie())){
            movies.add(movie.getIdMovie());
            favouriteButton.setImageResource(R.drawable.baseline_favorite_24);
        }else{
            movies.remove(Integer.valueOf(movie.getIdMovie()));
            favouriteButton.setImageResource(R.drawable.baseline_favorite_border_24);
        }
        movieViewModel.updateFavouriteFilms(movies);
    }

    private void isMovieInFavourite(){
        movieViewModel.getFavouriteFilms().observe(this, movies -> {
            if(movies != null && movies.contains(movie.getIdMovie())){
                favouriteButton.setImageResource(R.drawable.baseline_favorite_24);
            }
        });
    }
}