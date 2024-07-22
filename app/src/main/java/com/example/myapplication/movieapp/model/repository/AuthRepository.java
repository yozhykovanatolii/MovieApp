package com.example.myapplication.movieapp.model.repository;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.myapplication.movieapp.model.firebase.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

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

    public LiveData<String> signInWithLoginAndPassword(String login, String password){
        MutableLiveData<String> token = new MutableLiveData<>();
        firestore.collection("users").whereEqualTo("login", login).whereEqualTo("password", password).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful() && !task.getResult().isEmpty()){
                    for (QueryDocumentSnapshot documentSnapshot: task.getResult()){
                        token.setValue(getAuthorizationUserToken(documentSnapshot.toObject(User.class).getEmail(), password));
                    }
                }else{
                    token.setValue(null);
                }
            }
        });
        return token;
    }

    public String getAuthorizationUserToken(String email, String password){
        MutableLiveData<String> token = new MutableLiveData<>();
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                token.setValue(firebaseUser.getUid());
            }else{
                token.setValue(null);
            }
        });
        return token.getValue();
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

    public void signUp(User newUser){
        firestore.collection("users").document(newUser.getId()).set(newUser).addOnCompleteListener(task -> {
            if(!task.isSuccessful()){
                Log.i("SignUp", "Error", task.getException());
            }
        });
    }

}
