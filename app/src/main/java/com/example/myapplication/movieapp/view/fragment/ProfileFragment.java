package com.example.myapplication.movieapp.view.fragment;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.movieapp.R;
import com.example.myapplication.movieapp.model.firebase.User;
import com.example.myapplication.movieapp.view.activity.MainActivity;
import com.example.myapplication.movieapp.viewmodel.AuthViewModel;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textfield.TextInputEditText;
import com.squareup.picasso.Picasso;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

import dagger.hilt.android.AndroidEntryPoint;
import dev.sagar.progress_button.ProgressButton;

@AndroidEntryPoint
public class ProfileFragment extends Fragment {
    private AuthViewModel authViewModel;
    private User currentUser;
    private Uri urlPhoto;
    private TextInputEditText profileLogin, profileFullName, profileEmail, profilePassword;
    private TextView incorrectLoginProfile, incorrectFullNameProfile, incorrectEmailProfile, incorrectPasswordProfile;
    private ProgressButton signOutButton, updateProfileButton;
    private ShapeableImageView userPhoto;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initComponents(view);
        userPhoto.setOnClickListener(this::changePhoto);
        signOutButton.setOnClickListener(this::signOut);
        updateProfileButton.setOnClickListener(this::updateProfile);
        getDataAboutUser();
        editUsersLogin();
        editUsersFullName();
        editUsersEmail();
        editUsersPassword();
    }

    private void initComponents(View view){
        initTextInputEditTexts(view);
        initTextsView(view);
        initProgressButtons(view);
        userPhoto = view.findViewById(R.id.userPhoto);
    }

    private void initTextInputEditTexts(View view){
        profileLogin = view.findViewById(R.id.profileLogin);
        profileFullName = view.findViewById(R.id.profileFullName);
        profileEmail = view.findViewById(R.id.profileEmail);
        profilePassword = view.findViewById(R.id.profilePassword);
    }

    private void initTextsView(View view){
        incorrectLoginProfile = view.findViewById(R.id.incorrectLoginProfile);
        incorrectFullNameProfile = view.findViewById(R.id.incorrectFullNameProfile);
        incorrectEmailProfile = view.findViewById(R.id.incorrectEmailProfile);
        incorrectPasswordProfile = view.findViewById(R.id.incorrectPasswordProfile);
    }

    private void initProgressButtons(View view){
        signOutButton = view.findViewById(R.id.signOut);
        updateProfileButton = view.findViewById(R.id.updateProfile);
    }

    private void getDataAboutUser(){
        authViewModel.getUserById().observe(getViewLifecycleOwner(), user -> {
            if(user != null){
                initComponentsData(user);
            }
        });
    }

    private void initComponentsData(User user){
        currentUser = user;
        Picasso.get().load(user.getPhoto()).into(userPhoto);
        profileLogin.setText(user.getLogin());
        profileFullName.setText(user.getFullName());
        profileEmail.setText(user.getEmail());
        profilePassword.setText(user.getPassword());
    }

    private void editUsersLogin(){
        profileLogin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String login = Objects.requireNonNull(profileLogin.getText()).toString();
                if(!login.equals(currentUser.getLogin())){
                    checkUsersLogin(login);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });
    }

    private void editUsersFullName(){
        profileFullName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String fullName = Objects.requireNonNull(profileFullName.getText()).toString();
                if(checkUsersFullName(fullName)){
                    incorrectFullNameProfile.setVisibility(View.GONE);
                }else{
                    incorrectFullNameProfile.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });
    }

    private void editUsersEmail(){
        profileEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String email = Objects.requireNonNull(profileEmail.getText()).toString();
                if(checkUsersEmail(email)){
                    incorrectEmailProfile.setVisibility(View.GONE);
                }else{
                    incorrectEmailProfile.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });
    }

    private void editUsersPassword(){
        profilePassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String password = Objects.requireNonNull(profilePassword.getText()).toString();
                if(checkUsersPassword(password)){
                    incorrectPasswordProfile.setVisibility(View.GONE);
                }else{
                    incorrectPasswordProfile.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });
    }

    private void checkUsersLogin(String login){
        authViewModel.getUserByLogin(login).observe(getViewLifecycleOwner(), user -> {
            if(user == null){
                incorrectLoginProfile.setVisibility(View.GONE);
            }else{
                incorrectLoginProfile.setVisibility(View.VISIBLE);
            }
        });
    }

    private boolean checkUsersFullName(String fullName){
        AtomicBoolean isFullNameCorrect = new AtomicBoolean(false);
        authViewModel.isFullNameWriteCorrect(fullName).observe(this, aBoolean -> {
            if(aBoolean){
                isFullNameCorrect.set(true);
            }
        });
        return isFullNameCorrect.get();
    }

    private boolean checkUsersEmail(String email){
        AtomicBoolean isEmailCorrect = new AtomicBoolean(false);
        authViewModel.isEmailWriteCorrect(email).observe(this, aBoolean -> {
            if(aBoolean){
                isEmailCorrect.set(true);
            }
        });
        return isEmailCorrect.get();
    }

    private boolean checkUsersPassword(String password){
        AtomicBoolean isPasswordCorrect = new AtomicBoolean(false);
        authViewModel.isPasswordWriteCorrect(password).observe(this, aBoolean -> {
            if(aBoolean){
                isPasswordCorrect.set(true);
            }
        });
        return isPasswordCorrect.get();
    }

    public void changePhoto(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK);
        startActivityForResult(intent, 1000);
    }

    public void signOut(View view){
        signOutButton.loading();
        authViewModel.signOut();
        Intent intent = new Intent(getContext(), MainActivity.class);
        startActivity(intent);
    }

    public void updateProfile(View view){
        String login = Objects.requireNonNull(profileLogin.getText()).toString();
        updateProfileButton.loading();
        if(login.equals(currentUser.getLogin())){
            updateEmailAndPasswordUsers(login);
        }else{
            isLoginExist(login);
        }
    }

    private void isLoginExist(String login){
        authViewModel.getUserByLogin(login).observe(getViewLifecycleOwner(), user -> {
            if(user != null){
                updateProfileButton.finished();
                incorrectLoginProfile.setVisibility(View.VISIBLE);
            }else{
                incorrectLoginProfile.setVisibility(View.GONE);
                updateEmailAndPasswordUsers(login);
            }
        });
    }

    private void shorErrorMessages(){
        incorrectFullNameProfile.setVisibility(View.VISIBLE);
        incorrectEmailProfile.setVisibility(View.VISIBLE);
        incorrectPasswordProfile.setVisibility(View.VISIBLE);
    }

    private void hideErrorMessages(){
        incorrectFullNameProfile.setVisibility(View.GONE);
        incorrectEmailProfile.setVisibility(View.GONE);
        incorrectPasswordProfile.setVisibility(View.GONE);
    }

    private void updatePhoto(){
        if(urlPhoto != null){
            currentUser.setPhoto(urlPhoto.toString());
            authViewModel.saveImageInStorage(currentUser);
        }else{
            authViewModel.updateUser(currentUser);
        }
        updateProfileButton.finished();
        Toast.makeText(getContext(), "Profile was updated", Toast.LENGTH_LONG).show();
    }

    private void updateUser(String login, String fullName, String email, String password){
        currentUser.setLogin(login);
        currentUser.setFullName(fullName);
        currentUser.setEmail(email);
        currentUser.setPassword(password);
        updatePhoto();
    }

    private void updateEmailAndPasswordUsers(String login){
        String fullName = Objects.requireNonNull(profileFullName.getText()).toString();
        String email = Objects.requireNonNull(profileEmail.getText()).toString();
        String password = Objects.requireNonNull(profilePassword.getText()).toString();
        if(checkUsersFullName(fullName) && checkUsersEmail(email) && checkUsersPassword(password)){
            hideErrorMessages();
            authViewModel.updateEmailAndPassword(email, password, currentUser.getEmail(), currentUser.getPassword());
            updateUser(login, fullName, email, password);
        }else{
            updateProfileButton.finished();
            shorErrorMessages();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && requestCode == 1000 && data != null){
            urlPhoto = data.getData();
            userPhoto.setImageURI(data.getData());
        }
    }


}