package com.example.myapplication.movieapp.view.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.myapplication.movieapp.R;
import com.example.myapplication.movieapp.model.firebase.User;
import com.example.myapplication.movieapp.viewmodel.AuthViewModel;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MainWindowActivity extends AppCompatActivity {
    private AuthViewModel authViewModel;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_window);
        button = findViewById(R.id.button2);
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
        //getUser();
    }

    private void getUser(){
        authViewModel.getUserById().observe(this, user -> {
            if(user != null){
                Toast.makeText(getApplicationContext(), user.getLogin(), Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(getApplicationContext(), "Oops something wrong", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void goToMainActivity(View view){
        authViewModel.signOut();
        Intent intent = new Intent(view.getContext(), MainActivity.class);
        startActivity(intent);
    }
}