package com.example.myapplication.movieapp.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.myapplication.movieapp.model.firebase.User;
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

    public void createUserWithEmailAndPassword(User newUser){
        authRepository.createFirebaseUserByEmailAndPassword(newUser);
    }

    public void saveUserPhotoInFirebaseStorage(User user){
        authRepository.saveUserPhotoInFirebaseStorage(user);
    }

    public void signOut(){
        authRepository.signOut();
    }

    public LiveData<User> getCurrentUser(){
        return authRepository.getCurrentUser();
    }

    public LiveData<User> getUserByLogin(String login){
        return authRepository.getUserByLogin(login);
    }

    public LiveData<User> getUserByEmail(String email){
        return authRepository.getUserByEmail(email);
    }

    public LiveData<String> getUserEmailByLoginAndPassword(String login, String password){
        return authRepository.getUserEmailByLoginAndPassword(login, password);
    }

    public LiveData<String> getAuthorizationToken(String email, String password){
        return authRepository.getAuthorizationToken(email, password);
    }

    public void updateFirebaseUserPassword(User user, String newPassword){
        authRepository.updateFirebaseUserPassword(user, newPassword);
    }

    public void updateEmailAndPassword(String newEmail, String newPassword, String email, String password){
        authRepository.updateFirebaseUserEmailAndPassword(newEmail, newPassword, email, password);
    }

    public void saveUserInFirestore(User user){
        authRepository.saveUserInFirestore(user);
    }

    public LiveData<Boolean> isPasswordConfirm(String newPassword, String confirmPassword){
        return new MutableLiveData<>(isPasswordsEqual(newPassword, confirmPassword));
    }

    public LiveData<Boolean> isPasswordWriteCorrect(String password){
        return new MutableLiveData<>(isPasswordLargeThanOrEqualEight(password));
    }

    public LiveData<Boolean> isEmailWriteCorrect(String email){
        return new MutableLiveData<>(isEmailHasSignAndGmail(email));
    }

    public LiveData<Boolean> isFullNameWriteCorrect(String fullName){
        return new MutableLiveData<>(isFullNameHasNumbersAndSpecialSymbols(fullName));
    }

    public boolean isPasswordsEqual(String newPassword, String confirmPassword){
        return newPassword.equals(confirmPassword);
    }

    public boolean isPasswordLargeThanOrEqualEight(String password){
        return password.length() >= 8;
    }

    public boolean isEmailHasSignAndGmail(String email){
        return email.contains("@gmail.com") && !email.startsWith("@gmail.com");
    }

    public boolean isFullNameHasNumbersAndSpecialSymbols(String fullName){
        return fullName.matches(".*\\d.*") || fullName.matches(".*[!@#$%^&*()\\-+=\\[\\]{};:'\",.<>?/\\\\|`~].*");
    }

}
