package com.example.myapplication.movieapp.view.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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

import java.util.concurrent.atomic.AtomicBoolean;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MainActivity2 extends AppCompatActivity {
    private AuthViewModel authViewModel;
    private Button button;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
        button = findViewById(R.id.button3);
    }

    public void resetPassword(View view){
        authViewModel.getUserByEmail("yozhykovanatoliy@gmail.com").observe(this, user -> {
            if(user != null){
                updatePasswordInFirebaseUser(user);
            }
        });
    }

    private void updatePasswordInFirebaseUser(User user){
        authViewModel.updatePasswordInFirebaseUser(user, "57naruto");
    }

    private void updatePassword(User user){
        authViewModel.getAuthorizationUserToken(user.getEmail(), user.getPassword()).observe(this, id -> {
            if(id != null){
                //updatePassword(user);
            }
        });
        authViewModel.signOut();
        Toast.makeText(getApplicationContext(), "Password update", Toast.LENGTH_LONG).show();
    }
}