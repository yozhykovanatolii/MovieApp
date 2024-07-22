package com.example.myapplication.movieapp.view.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.myapplication.movieapp.R;
import com.example.myapplication.movieapp.model.firebase.User;
import com.example.myapplication.movieapp.model.remote.Movie;
import com.example.myapplication.movieapp.viewmodel.AuthViewModel;
import com.example.myapplication.movieapp.viewmodel.MovieViewModel;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {
    private AuthViewModel authViewModel;
    private EditText editText;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText = findViewById(R.id.editTextText);
        textView = findViewById(R.id.error);
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
        //sighUp();
    }



    private void sighUp(){
        authViewModel.createUserWithEmailAndPassword(new User("Yozhykov", "yozhyk", "123456", "gamet5550@gmail.com", "38058585"));
    }

}