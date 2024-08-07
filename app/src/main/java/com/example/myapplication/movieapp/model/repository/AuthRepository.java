package com.example.myapplication.movieapp.model.repository;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.myapplication.movieapp.model.firebase.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import javax.inject.Inject;

public class AuthRepository {
    private FirebaseFirestore firestore;
    private FirebaseAuth firebaseAuth;
    private FirebaseStorage firebaseStorage;

    @Inject
    public AuthRepository(FirebaseFirestore firestore, FirebaseAuth firebaseAuth, FirebaseStorage firebaseStorage) {
        this.firestore = firestore;
        this.firebaseAuth = firebaseAuth;
        this.firebaseStorage = firebaseStorage;
    }

    public LiveData<User> getCurrentUser(){
        MutableLiveData<User> userMutableLiveData = new MutableLiveData<>();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if(firebaseUser == null){
            userMutableLiveData.setValue(null);
            return userMutableLiveData;
        }
        loadUserFromFirestore(firebaseUser.getUid(), userMutableLiveData);
        return userMutableLiveData;
    }

    private void loadUserFromFirestore(String userId, MutableLiveData<User> userMutableLiveData){
        firestore.collection("users").document(userId).get().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                userMutableLiveData.setValue(task.getResult().toObject(User.class));
            }else{
                userMutableLiveData.setValue(null);
            }
        });
    }

    public LiveData<User> getUserByEmail(String email){
        MutableLiveData<User> userMutableLiveData = new MutableLiveData<>();
        firestore.collection("users").whereEqualTo("email", email).get().addOnCompleteListener(task -> {
            if(task.isSuccessful() && !task.getResult().isEmpty()){
                for(QueryDocumentSnapshot documentSnapshot: task.getResult()){
                    userMutableLiveData.setValue(documentSnapshot.toObject(User.class));
                }
            }else{
                userMutableLiveData.setValue(null);
            }
        });
        return userMutableLiveData;
    }

    public LiveData<User> getUserByLogin(String login){
        MutableLiveData<User> userMutableLiveData = new MutableLiveData<>();
        firestore.collection("users").whereEqualTo("login", login).get().addOnCompleteListener(task -> {
            if(task.isSuccessful() && !task.getResult().isEmpty()){
                for(QueryDocumentSnapshot documentSnapshot: task.getResult()){
                    userMutableLiveData.setValue(documentSnapshot.toObject(User.class));
                }
            }else{
                userMutableLiveData.setValue(null);
            }
        });
        return userMutableLiveData;
    }

    public LiveData<String> getUserEmailByLoginAndPassword(String login, String password){
        MutableLiveData<String> email = new MutableLiveData<>();
        firestore.collection("users").whereEqualTo("login", login).whereEqualTo("password", password).get().addOnCompleteListener(task -> {
            if(task.isSuccessful() && !task.getResult().isEmpty()){
                for (QueryDocumentSnapshot documentSnapshot: task.getResult()){
                    email.setValue(documentSnapshot.toObject(User.class).getEmail());
                }
            }else{
                email.setValue(null);
            }
        });
        return email;
    }

    public LiveData<String> getAuthorizationToken(String email, String password){
        MutableLiveData<String> token = new MutableLiveData<>();
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if(!task.isSuccessful()){
                token.setValue(null);
                Log.i("AuthorizationToken", "Exception:", task.getException());
            }else{
                FirebaseUser currentUser = firebaseAuth.getCurrentUser();
                token.setValue(currentUser.getUid());
            }
        });
        return token;
    }

    public void updateFirebaseUserPassword(User user, String newPassword){
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), user.getPassword());
        if(firebaseUser != null){
            firebaseUser.reauthenticate(credential).addOnCompleteListener(task -> {
                if(task.isSuccessful()){
                    firebaseUser.updatePassword(newPassword);
                    updateUserPasswordInFirestore(newPassword, user.getId());
                }
            });
        }
    }

    private void updateUserPasswordInFirestore(String newPassword, String userId){
        firestore.collection("users").document(userId).update("password", newPassword).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(!task.isSuccessful()){
                    Log.i("UpdatePassword", "Password didn't reset");
                }
            }
        });
    }

    public void createFirebaseUserByEmailAndPassword(User newUser){
        firebaseAuth.createUserWithEmailAndPassword(newUser.getEmail(), newUser.getPassword()).addOnCompleteListener(task -> {
            if(!task.isSuccessful()){
                Log.i("FirebaseUser", "Exception:", task.getException());
            }else{
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user != null){
                    newUser.setId(user.getUid());
                    saveUserPhotoInFirebaseStorage(newUser);
                }
            }
        });
    }

    public void updateFirebaseUserEmailAndPassword(String newEmail, String newPassword, String email, String password){
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        AuthCredential credential = EmailAuthProvider.getCredential(email, password);
        if(firebaseUser != null){
            firebaseUser.reauthenticate(credential).addOnCompleteListener(task -> {
                if(task.isSuccessful()){
                    firebaseUser.updatePassword(newPassword);
                    firebaseUser.updateEmail(newEmail);
                }
            });
        }
    }

    public void saveUserPhotoInFirebaseStorage(User newUser){
        Uri image = Uri.parse(newUser.getPhoto());
        StorageReference storageReference = firebaseStorage.getReference().child("image/" + image.getLastPathSegment());
        storageReference.putFile(image).addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                storageReference.getDownloadUrl().addOnCompleteListener(task1 -> {
                    newUser.setPhoto(task1.getResult().toString());
                    saveUserInFirestore(newUser);
                });
            }else{
                Log.i("SaveUserPhoto", "Exception:", task.getException());
            }
        });
    }

    public void saveUserInFirestore(User newUser){
        firestore.collection("users").document(newUser.getId()).set(newUser).addOnCompleteListener(task -> {
            if(!task.isSuccessful()){
                Log.i("SaveUser", "Exception:", task.getException());
            }
        });
    }

    public void signOut(){
        firebaseAuth.signOut();
    }
}
