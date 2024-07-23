package com.example.myapplication.movieapp.view.activity;

import android.os.Bundle;
import android.view.View;
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
public class MainActivity3 extends AppCompatActivity {
    private AuthViewModel authViewModel;
    private User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
        getDataAboutUser();
    }

    private void getDataAboutUser(){
        authViewModel.getUserById().observe(this, user -> {
            if(user != null){
                currentUser = user;
            }
        });
    }

    public void updateDataAboutUser(View view){
        Toast.makeText(getApplicationContext(), currentUser.getEmail() + " " + currentUser.getPassword(), Toast.LENGTH_LONG).show();
        //currentUser.setLogin("andrey74");
        currentUser.setFullName("Yozhykov Tolya");
        authViewModel.updateEmailAndPassword("tolya2003@gmail.com", currentUser.getPassword(), currentUser.getEmail(), currentUser.getPassword());
        currentUser.setEmail("tolya2003@gmail.com");
        authViewModel.updateUser(currentUser);
        Toast.makeText(getApplicationContext(), "Some text", Toast.LENGTH_LONG).show();
    }


}