package com.example.myapplication.movieapp.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.myapplication.movieapp.R;
import com.example.myapplication.movieapp.viewmodel.AuthViewModel;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class SplashScreenActivity extends AppCompatActivity {
    private AuthViewModel authViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
    }

    @Override
    protected void onStart() {
        super.onStart();
        authViewModel.getCurrentUser().observe(this, user -> {
            if(user == null){
                usingIntent(MainActivity.class);
            }else{
                usingIntent(MainWindowActivity.class);
            }
        });
    }

    private void usingIntent(Class<?> currentClass){
        new Handler().postDelayed(() -> {
            Intent intent = new Intent(SplashScreenActivity.this, currentClass);
            startActivity(intent);
        }, 2000);
    }
}