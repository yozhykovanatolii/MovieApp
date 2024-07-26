package com.example.myapplication.movieapp.view.activity;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.myapplication.movieapp.R;
import com.example.myapplication.movieapp.model.firebase.User;
import com.example.myapplication.movieapp.receiver.InternetReceiver;
import com.example.myapplication.movieapp.viewmodel.AuthViewModel;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

import dagger.hilt.android.AndroidEntryPoint;
import dev.sagar.progress_button.ProgressButton;

@AndroidEntryPoint
public class SignUpActivity extends AppCompatActivity {
    private AuthViewModel authViewModel;
    private Uri uri;
    private BroadcastReceiver broadcastReceiver;
    private TextInputEditText signUpLogin, signUpFullName, signUpEmail, signUpPassword;
    private TextView incorrectLogin, incorrectFullName, incorrectEmail, incorrectPassword, photoNotChoose;
    private ProgressButton progressButtonInSignUp;
    private ShapeableImageView avatarSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
        broadcastReceiver = new InternetReceiver();
        initComponents();
        editLoginInSignUp();
        editFullNameInSignUp();
        editEmailInSignUp();
        editPasswordInSignUp();
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
        initTextInputEditTexts();
        initTextsView();
        progressButtonInSignUp = findViewById(R.id.progressButtonInSignUp);
        avatarSignUp = findViewById(R.id.avatarSignUp);
    }

    private void initTextInputEditTexts(){
        signUpLogin = findViewById(R.id.signUpLogin);
        signUpFullName = findViewById(R.id.signUpFullName);
        signUpEmail = findViewById(R.id.signUpEmail);
        signUpPassword = findViewById(R.id.signUpPassword);
    }

    private void initTextsView(){
        incorrectLogin = findViewById(R.id.incorrectLogin);
        incorrectFullName = findViewById(R.id.incorrectFullName);
        incorrectEmail = findViewById(R.id.incorrectEmail);
        incorrectPassword = findViewById(R.id.incorrectPassword);
        photoNotChoose = findViewById(R.id.photoNotChoose);
    }

    private void editLoginInSignUp(){
        signUpLogin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                checkLogin(Objects.requireNonNull(signUpLogin.getText()).toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });
    }

    private void editFullNameInSignUp(){
        signUpFullName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(checkFullName(Objects.requireNonNull(signUpFullName.getText()).toString())){
                    incorrectFullName.setVisibility(View.GONE);
                }else{
                    incorrectFullName.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });
    }

    private void editEmailInSignUp(){
        signUpEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(checkEmail(Objects.requireNonNull(signUpEmail.getText()).toString())){
                    incorrectEmail.setVisibility(View.GONE);
                }else{
                    incorrectEmail.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });
    }

    private void editPasswordInSignUp(){
        signUpPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(checkPassword(Objects.requireNonNull(signUpPassword.getText()).toString())){
                    incorrectPassword.setVisibility(View.GONE);
                }else{
                    incorrectPassword.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });
    }

    private void checkLogin(String login){
        authViewModel.getUserByLogin(login).observe(this, user -> {
            if(user != null){
                incorrectLogin.setVisibility(View.VISIBLE);
            }else{
                incorrectLogin.setVisibility(View.GONE);
            }
        });
    }

    private boolean checkFullName(String fullName){
        AtomicBoolean isFullNameCorrect = new AtomicBoolean(false);
        authViewModel.isFullNameWriteCorrect(fullName).observe(this, aBoolean -> {
            if(aBoolean){
                isFullNameCorrect.set(true);
            }
        });
        return isFullNameCorrect.get();
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
        incorrectFullName.setVisibility(View.VISIBLE);
        incorrectEmail.setVisibility(View.VISIBLE);
        incorrectPassword.setVisibility(View.VISIBLE);
        photoNotChoose.setVisibility(View.VISIBLE);
    }

    private void hideErrorMessages(){
        incorrectFullName.setVisibility(View.GONE);
        incorrectEmail.setVisibility(View.GONE);
        incorrectPassword.setVisibility(View.GONE);
        photoNotChoose.setVisibility(View.GONE);
    }

    public void pickUpImage(View view){
        Intent intent = new Intent(Intent.ACTION_PICK);
        startActivityForResult(intent, 1000);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            if(requestCode == 1000){
                if(data != null){
                    uri = data.getData();
                    avatarSignUp.setImageURI(data.getData());
                }
            }
        }
    }

    public void signUp(View view){
        String login = Objects.requireNonNull(signUpLogin.getText()).toString();
        progressButtonInSignUp.loading();
        authViewModel.getUserByLogin(login).observe(this, user -> {
            if(user != null){
                progressButtonInSignUp.finished();
                incorrectLogin.setVisibility(View.VISIBLE);
            }else{
                incorrectLogin.setVisibility(View.GONE);
                createUser(login);
            }
        });
    }

    private void createUser(String login){
        String fullName = Objects.requireNonNull(signUpFullName.getText()).toString();
        String email = Objects.requireNonNull(signUpEmail.getText()).toString();
        String password = Objects.requireNonNull(signUpPassword.getText()).toString();
        if(checkFullName(fullName) && checkEmail(email) && checkPassword(password) && uri != null){
            hideErrorMessages();
            User user = new User(login, fullName, email, password, uri.toString());
            goToMainActivity(user);
        }else{
            progressButtonInSignUp.finished();
            showErrorMessages();
        }
    }

    private void goToMainActivity(User user){
        authViewModel.createUserWithEmailAndPassword(user);
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}