package com.example.myapplication.movieapp.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.myapplication.movieapp.model.firebase.User;
import com.example.myapplication.movieapp.model.repository.AuthRepository;

import java.util.regex.Pattern;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class AuthViewModel extends ViewModel {
    private AuthRepository authRepository;

    @Inject
    public AuthViewModel(AuthRepository authRepository){
        this.authRepository = authRepository;
    }

    public void createUserWithEmailAndPassword(User newUser){
        authRepository.createUserWithEmailAndPassword(newUser);
    }

    public LiveData<User> getUserByLogin(String login){
        return authRepository.getUserByLogin(login);
    }

    public LiveData<String> signInWithLoginAndPassword(String login, String password){
        return null;
    }

    public LiveData<Boolean> isPasswordWriteCorrect(String password){
        if(isPasswordLargeThanOrEqualEight(password)){
            return new MutableLiveData<>(true);
        }else{
            return new MutableLiveData<>(false);
        }
    }

    public LiveData<Boolean> isEmailWriteCorrect(String email){
        if(isEmailHasSignAndGmail(email)){
            return new MutableLiveData<>(true);
        }else{
            return new MutableLiveData<>(false);
        }
    }

    public LiveData<Boolean> isPhoneNumberWriteCorrect(String phoneNumber){
        if(isPhoneNumberCorrect(phoneNumber)){
            return new MutableLiveData<>(true);
        }else{
            return new MutableLiveData<>(false);
        }
    }

    public LiveData<Boolean> isFullNameWriteCorrect(String fullName){
        if(isFullNameHasNumbersAndSpecialSymbols(fullName)){
            return new MutableLiveData<>(false);
        }else{
            return new MutableLiveData<>(true);
        }
    }

    public boolean isPasswordLargeThanOrEqualEight(String password){
        return password.length() >= 8;
    }

    public boolean isEmailHasSignAndGmail(String email){
        return email.contains("@gmail.com") && !email.startsWith("@gmail.com");
    }

    public boolean isPhoneNumberCorrect(String phoneNumber){
        String pattern = "^\\+380\\d{9}$";
        return Pattern.matches(pattern, phoneNumber);
    }

    public boolean isFullNameHasNumbersAndSpecialSymbols(String fullName){
        return fullName.matches(".*\\d.*") || fullName.matches(".*[!@#$%^&*()\\-+=\\[\\]{};:'\",.<>?/\\\\|`~].*");
    }

}
