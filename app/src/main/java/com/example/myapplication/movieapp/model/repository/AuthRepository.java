package com.example.myapplication.movieapp.model.repository;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

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


}
