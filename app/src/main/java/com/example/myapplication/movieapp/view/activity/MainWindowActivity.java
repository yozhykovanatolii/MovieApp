package com.example.myapplication.movieapp.view.activity;


import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.myapplication.movieapp.R;
import com.example.myapplication.movieapp.model.firebase.User;
import com.example.myapplication.movieapp.receiver.InternetReceiver;
import com.example.myapplication.movieapp.view.fragment.FavouriteMoviesFragment;
import com.example.myapplication.movieapp.view.fragment.HomeFragment;
import com.example.myapplication.movieapp.view.fragment.ProfileFragment;
import com.example.myapplication.movieapp.view.fragment.SearchMovieFragment;
import com.example.myapplication.movieapp.viewmodel.MovieViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MainWindowActivity extends AppCompatActivity {
    private MovieViewModel movieViewModel;
    private BottomNavigationView navigationView;
    private BroadcastReceiver broadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_window);
        movieViewModel = new ViewModelProvider(this).get(MovieViewModel.class);
        broadcastReceiver = new InternetReceiver();
        navigationView = findViewById(R.id.navigationBottom);
        getCurrentUser();
        showFragment(savedInstanceState);
        navigationView.setOnItemSelectedListener(this::onItemClicked);
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

    private void showFragment(Bundle savedInstanceState){
        if(savedInstanceState == null){
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainer, HomeFragment.class, null)
                    .commit();
        }
    }

    private void getCurrentUser(){
        movieViewModel.getCurrentUser().observe(this, user -> {
            if(user != null){
                loadAvatar(user.getPhoto());
            }
        });
    }

    private void loadAvatar(String photoPath){
        navigationView.setItemIconTintList(null);
        Glide.with(this)
                .load(photoPath)
                .apply(RequestOptions.circleCropTransform()
                        .placeholder(R.drawable.baseline_account_circle_24))
                .into(new CustomTarget<Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        navigationView.getMenu().findItem(R.id.profile).setIcon(resource);
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {}
                });
    }

    private boolean onItemClicked(MenuItem item){
        if(item.getItemId() == R.id.home){
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainer, HomeFragment.class, null)
                    .setReorderingAllowed(true)
                    .commit();
            return true;
        }if(item.getItemId() == R.id.search){
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainer, SearchMovieFragment.class, null)
                    .setReorderingAllowed(true)
                    .commit();
            return true;
        }if(item.getItemId() == R.id.favorite){
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainer, FavouriteMoviesFragment.class, null)
                    .setReorderingAllowed(true)
                    .commit();
            return true;
        }if(item.getItemId() == R.id.profile){
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainer, ProfileFragment.class, null)
                    .setReorderingAllowed(true)
                    .commit();
            return true;
        }
        return false;
    }


    private void getFavouriteMovies(){
    }

    private void checkMovieInFavourites(List<Integer> movies){
        if(!movies.contains(375)){
            movies.add(375);
            //button.setBackgroundColor(Color.parseColor("#1877F2"));
        }else{
            movies.remove(Integer.valueOf(375));
            //button.setBackgroundColor(Color.parseColor("#FF0000"));
        }
        movieViewModel.updateFavouriteFilms(movies);
    }

    public void addToFavourite(View view){
        movieViewModel.getFavouriteFilms().observe(this, movies -> {
            if(movies != null){
                //checkMovieInFavourites(movies);
            }
        });
    }

    private void isMovieInFavourite(){
        movieViewModel.getFavouriteFilms().observe(this, movies -> {
            if(movies != null && movies.contains(375)){
                //button.setBackgroundColor(Color.parseColor("#1877F2"));
            }
        });
    }
}