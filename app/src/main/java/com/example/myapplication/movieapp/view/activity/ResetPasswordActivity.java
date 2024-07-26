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
import com.example.myapplication.movieapp.receiver.InternetReceiver;
import com.example.myapplication.movieapp.viewmodel.AuthViewModel;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

import dagger.hilt.android.AndroidEntryPoint;
import dev.sagar.progress_button.ProgressButton;

@AndroidEntryPoint
public class ResetPasswordActivity extends AppCompatActivity {
    private AuthViewModel authViewModel;
    private User user;
    private BroadcastReceiver broadcastReceiver;
    private TextInputEditText resetPasswordNewPassword, resetPasswordConfirmPassword;
    private TextView errorPasswordInResetPassword, errorConfirmPasswordInResetPassword;
    private ProgressButton progressButtonInResetPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
        user = (User) getIntent().getSerializableExtra("User");
        broadcastReceiver = new InternetReceiver();
        initComponents();
        editNewPassword();
        editConfirmedPassword();
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
        resetPasswordNewPassword = findViewById(R.id.resetPasswordNewPassword);
        resetPasswordConfirmPassword = findViewById(R.id.resetPasswordConfirmPassword);
        errorPasswordInResetPassword = findViewById(R.id.errorPasswordInResetPassword);
        errorConfirmPasswordInResetPassword = findViewById(R.id.errorConfirmPasswordInResetPassword);
        progressButtonInResetPassword = findViewById(R.id.progressButtonInResetPassword);
    }

    private void editNewPassword(){
        resetPasswordNewPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(checkNewPassword(Objects.requireNonNull(resetPasswordNewPassword.getText()).toString())){
                    errorPasswordInResetPassword.setVisibility(View.GONE);
                }else{
                    errorPasswordInResetPassword.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });
    }

    private void editConfirmedPassword(){
        resetPasswordConfirmPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String newPassword = Objects.requireNonNull(resetPasswordNewPassword.getText()).toString();
                String confirmedPassword = Objects.requireNonNull(resetPasswordConfirmPassword.getText()).toString();
                if(checkConfirmedPassword(newPassword, confirmedPassword)){
                    errorConfirmPasswordInResetPassword.setVisibility(View.GONE);
                }else{
                    errorConfirmPasswordInResetPassword.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });
    }

    private boolean checkNewPassword(String newPassword){
        AtomicBoolean isNewPasswordCorrect = new AtomicBoolean(false);
        authViewModel.isPasswordWriteCorrect(newPassword).observe(this, aBoolean -> {
            if(aBoolean){
                isNewPasswordCorrect.set(true);
            }
        });
        return isNewPasswordCorrect.get();
    }

    private boolean checkConfirmedPassword(String newPassword, String confirmedPassword){
        AtomicBoolean isPasswordConfirmed = new AtomicBoolean(false);
        authViewModel.isPasswordConfirm(newPassword, confirmedPassword).observe(this, aBoolean -> {
            if(aBoolean){
                isPasswordConfirmed.set(true);
            }
        });
        return isPasswordConfirmed.get();
    }

    private void showErrorMessages(){
        errorPasswordInResetPassword.setVisibility(View.VISIBLE);
        errorConfirmPasswordInResetPassword.setVisibility(View.VISIBLE);
    }

    private void hideErrorMessages(){
        errorPasswordInResetPassword.setVisibility(View.GONE);
        errorConfirmPasswordInResetPassword.setVisibility(View.GONE);
    }

    public void resetPassword(View view){
        String newPassword = Objects.requireNonNull(resetPasswordNewPassword.getText()).toString();
        String confirmedPassword = Objects.requireNonNull(resetPasswordConfirmPassword.getText()).toString();
        progressButtonInResetPassword.loading();
        if(checkNewPassword(newPassword) && checkConfirmedPassword(newPassword, confirmedPassword)){
            hideErrorMessages();
            getAuthorizationToken(newPassword);
        }else{
            progressButtonInResetPassword.finished();
            showErrorMessages();
        }
    }

    private void getAuthorizationToken(String newPassword){
        authViewModel.getAuthorizationUserToken(user.getEmail(), user.getPassword()).observe(this, id -> {
            if(id != null){
                updatePassword(newPassword);
            }
        });
    }

    private void updatePassword(String newPassword){
        authViewModel.updatePasswordInFirebaseUser(user, newPassword);
        authViewModel.signOut();
        Toast.makeText(getApplicationContext(), "Password update", Toast.LENGTH_LONG).show();
        goToMainActivity();
    }

    private void goToMainActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }



}