package com.example.myapplication.movieapp.view.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
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
import com.google.android.material.button.MaterialButton;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.android.material.textfield.TextInputEditText;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

import dagger.hilt.android.AndroidEntryPoint;
import dev.sagar.progress_button.ProgressButton;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {
    private AuthViewModel authViewModel;
    private TextInputEditText signInLogin, signInPassword;
    private TextView errorLoginSignIn, errorPasswordSignIn;
    private ProgressButton progressButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
        initComponents();
        editLoginInSignIn();
        editPasswordInSignIn();
    }

    private void initComponents(){
        signInLogin = findViewById(R.id.signInLogin);
        signInPassword = findViewById(R.id.signInPassword);
        errorLoginSignIn = findViewById(R.id.errorLoginSignIn);
        errorPasswordSignIn = findViewById(R.id.errorPasswordSignIn);
        progressButton = findViewById(R.id.progressButtonInSignIn);
    }

    private void editLoginInSignIn(){
        signInLogin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                checkLogin(Objects.requireNonNull(signInLogin.getText()).toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });
    }

    private void editPasswordInSignIn(){
        signInPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(checkPassword(Objects.requireNonNull(signInPassword.getText()).toString())){
                    errorPasswordSignIn.setVisibility(View.GONE);
                }else{
                    errorPasswordSignIn.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });
    }

    private void checkLogin(String login){
        authViewModel.getUserByLogin(login).observe(this, user -> {
            if(user != null){
                errorLoginSignIn.setVisibility(View.GONE);
            }else{
                errorLoginSignIn.setVisibility(View.VISIBLE);
            }
        });
    }

    private boolean checkPassword(String password){
        AtomicBoolean isPasswordCorrect = new AtomicBoolean(false);
        authViewModel.isPasswordWriteCorrect(password).observe(this, aBoolean -> {
            if(aBoolean){
                isPasswordCorrect.set(true);
            }
        });
        return isPasswordCorrect.get();
    }

    private void showErrorMessages(){
        errorLoginSignIn.setVisibility(View.VISIBLE);
        errorPasswordSignIn.setVisibility(View.VISIBLE);
    }

    private void hideErrorMessages(){
        errorLoginSignIn.setVisibility(View.GONE);
        errorPasswordSignIn.setVisibility(View.GONE);
    }

    public void signInUser(View view){
        String login = Objects.requireNonNull(signInLogin.getText()).toString();
        String password = Objects.requireNonNull(signInPassword.getText()).toString();
        hideErrorMessages();
        progressButton.loading();
        authViewModel.getUserEmailByLoginAndPassword(login, password).observe(this, email -> {
            if(email != null){
                hideErrorMessages();
                getAuthorizationToken(email, password);
            }else{
                progressButton.finished();
                showErrorMessages();
            }
        });
    }

    private void getAuthorizationToken(String email, String password){
        authViewModel.getAuthorizationUserToken(email, password).observe(this, token -> {
            if(token != null){
                System.out.println(token);
                goToMainWindow();
            }else{
                progressButton.finished();
                Toast.makeText(getApplicationContext(), "Oops something wrong", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void goToMainWindow(){
        Intent intent = new Intent(getApplicationContext(), MainActivity3.class);
        startActivity(intent);
    }

    public void goToForgotPasswordActivity(View view){
        Intent intent = new Intent(getApplicationContext(), ForgotPasswordActivity.class);
        startActivity(intent);
    }

    public void goToSignUpActivity(View view){
        Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
        startActivity(intent);
    }
}