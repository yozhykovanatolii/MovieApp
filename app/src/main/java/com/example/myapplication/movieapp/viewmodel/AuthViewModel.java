package com.example.myapplication.movieapp.viewmodel;

import androidx.lifecycle.ViewModel;

import com.example.myapplication.movieapp.model.repository.AuthRepository;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class AuthViewModel extends ViewModel {
    private AuthRepository authRepository;

    @Inject
    public AuthViewModel(AuthRepository authRepository){
        this.authRepository = authRepository;
    }


}
