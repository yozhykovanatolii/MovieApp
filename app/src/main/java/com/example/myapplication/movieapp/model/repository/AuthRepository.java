package com.example.myapplication.movieapp.model.repository;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.myapplication.movieapp.model.firebase.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.util.Objects;
import java.util.concurrent.Executor;

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

    public LiveData<User> getUserById(){
        MutableLiveData<User> userMutableLiveData = new MutableLiveData<>();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if(firebaseUser != null){
            firestore.collection("users").document(firebaseUser.getUid()).get().addOnCompleteListener(task -> {
                if(task.isSuccessful()){
                    userMutableLiveData.setValue(task.getResult().toObject(User.class));
                }else{
                    userMutableLiveData.setValue(null);
                }
            });
        }else{
            userMutableLiveData.setValue(null);
        }
        return userMutableLiveData;
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

    public LiveData<String> getAuthorizationUserToken(String email, String password){
        MutableLiveData<String> token = new MutableLiveData<>();
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if(!task.isSuccessful()){
                token.setValue(null);
                Log.i("Exceptions", "Error", task.getException());
            }else{
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                token.setValue(firebaseUser.getUid());
            }
        });
        return token;
    }

    public void updatePasswordInFirebaseUser(User user, String newPassword){
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), user.getPassword());
        if(firebaseUser != null){
            firebaseUser.reauthenticate(credential).addOnCompleteListener(task -> {
                if(task.isSuccessful()){
                    firebaseUser.updatePassword(newPassword);
                    updatePasswordInFirestore(newPassword, user.getId());
                }
            });
        }
    }

    private void updatePasswordInFirestore(String newPassword, String userId){
        firestore.collection("users").document(userId).update("password", newPassword).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(!task.isSuccessful()){
                    Log.i("UpdatePassword", "Password didn't reset");
                }
            }
        });
    }

    public void createUserWithEmailAndPassword(User newUser){
        firebaseAuth.createUserWithEmailAndPassword(newUser.getEmail(), newUser.getPassword()).addOnCompleteListener(task -> {
            if(!task.isSuccessful()){
                Log.i("Exceptions", "Error", task.getException());
            }else{
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user != null){
                    newUser.setId(user.getUid());
                    signUp(newUser);
                }
            }
        });
    }

    public void updateEmailAndPassword(String newEmail, String newPassword, String email, String password){
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        AuthCredential credential = EmailAuthProvider.getCredential(email, password);
        if(firebaseUser != null){
            firebaseUser.reauthenticate(credential).addOnCompleteListener(task -> {
                if(task.isSuccessful()){
                    //firebaseUser.updatePassword(newPassword);
                    firebaseUser.updateEmail(newEmail);
                }
            });
        }else{
            System.out.println("Error Firebase User");
        }
    }

    public void updateUser(User user){
        firestore.collection("users").document(user.getId()).set(user).addOnCompleteListener(task -> {
            if(!task.isSuccessful()){
                Log.i("UpdateUser", "Something wrong", task.getException());
            }
        });
    }

    public void signUp(User newUser){
        firestore.collection("users").document(newUser.getId()).set(newUser).addOnCompleteListener(task -> {
            if(!task.isSuccessful()){
                Log.i("SignUp", "Error", task.getException());
            }
        });
    }

    public void signOut(){
        firebaseAuth.signOut();
    }

}
