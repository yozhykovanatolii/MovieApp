package com.example.myapplication.movieapp.view.activity;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.myapplication.movieapp.R;
import com.example.myapplication.movieapp.model.firebase.User;
import com.example.myapplication.movieapp.receiver.InternetReceiver;
import com.example.myapplication.movieapp.viewmodel.AuthViewModel;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

import dagger.hilt.android.AndroidEntryPoint;
import dev.sagar.progress_button.ProgressButton;

@AndroidEntryPoint
public class ForgotPasswordActivity extends AppCompatActivity {
    private AuthViewModel authViewModel;
    private TextInputEditText forgotPasswordEmail;
    private TextView errorEmailInForgotPassword;
    private ProgressButton progressButtonInForgotPassword;
    private BroadcastReceiver broadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
        broadcastReceiver = new InternetReceiver();
        initComponents();
        editEmailInForgotPassword();
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
        forgotPasswordEmail = findViewById(R.id.forgotPasswordEmail);
        errorEmailInForgotPassword = findViewById(R.id.errorEmailInForgotPassword);
        progressButtonInForgotPassword = findViewById(R.id.progressButtonInForgotPassword);
    }

    private void editEmailInForgotPassword(){
        forgotPasswordEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(checkEmail(Objects.requireNonNull(forgotPasswordEmail.getText()).toString())){
                    errorEmailInForgotPassword.setVisibility(View.GONE);
                }else{
                    errorEmailInForgotPassword.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });
    }

    private boolean checkEmail(String email){
        AtomicBoolean isEmailCorrect = new AtomicBoolean(false);
        authViewModel.isEmailWriteCorrect(email).observe(this, aBoolean -> {
            if(aBoolean){
                isEmailCorrect.set(true);
            }
        });
        return isEmailCorrect.get();
    }

    public void submitEmail(View view){
        String email = Objects.requireNonNull(forgotPasswordEmail.getText()).toString();
        progressButtonInForgotPassword.loading();
        authViewModel.getUserByEmail(email).observe(this, user -> {
            if(user != null){
                errorEmailInForgotPassword.setVisibility(View.GONE);
                goToResetPasswordActivity(user);
            }else{
                progressButtonInForgotPassword.finished();
                errorEmailInForgotPassword.setVisibility(View.VISIBLE);
            }
        });
    }

    private void goToResetPasswordActivity(User user){
        Intent intent = new Intent(this, ResetPasswordActivity.class);
        intent.putExtra("User", user);
        startActivity(intent);
    }
}