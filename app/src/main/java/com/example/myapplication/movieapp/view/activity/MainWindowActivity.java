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
import androidx.fragment.app.Fragment;
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
import com.example.myapplication.movieapp.viewmodel.AuthViewModel;
import com.example.myapplication.movieapp.viewmodel.MovieViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MainWindowActivity extends AppCompatActivity {
    private AuthViewModel authViewModel;
    private BottomNavigationView navigationView;
    private BroadcastReceiver broadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_window);
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
        broadcastReceiver = new InternetReceiver();
        navigationView = findViewById(R.id.navigationBottom);
        showHomeFragment(savedInstanceState);
        getCurrentUser();
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

    private void showHomeFragment(Bundle savedInstanceState){
        if(savedInstanceState == null){
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainer, HomeFragment.class, null)
                    .commit();
        }
    }

    private void getCurrentUser(){
        authViewModel.getCurrentUser().observe(this, user -> {
            if(user != null){
                loadAvatar(user.getPhoto());
            }
        });
    }

    private void loadAvatar(String photoPath){
        navigationView.setItemIconTintList(null);
        Glide.with(this)
                .load(photoPath)
                .apply(RequestOptions.circleCropTransform().placeholder(R.drawable.baseline_account_circle_24))
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
            showFragment(HomeFragment.class);
            return true;
        }if(item.getItemId() == R.id.search){
            showFragment(SearchMovieFragment.class);
            return true;
        }if(item.getItemId() == R.id.favorite){
            showFragment(FavouriteMoviesFragment.class);
            return true;
        }if(item.getItemId() == R.id.profile){
            showFragment(ProfileFragment.class);
            return true;
        }
        return false;
    }

    private void showFragment(Class<? extends Fragment> fragment){
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, fragment, null)
                .setReorderingAllowed(true)
                .commit();
    }

}