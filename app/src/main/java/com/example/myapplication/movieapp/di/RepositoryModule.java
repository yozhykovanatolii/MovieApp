package com.example.myapplication.movieapp.di;

import com.example.myapplication.movieapp.model.remote.ApiPoint;
import com.example.myapplication.movieapp.model.repository.AuthRepository;
import com.example.myapplication.movieapp.model.repository.MovieRepository;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.ViewModelComponent;

@Module
@InstallIn(ViewModelComponent.class)
public class RepositoryModule {

    @Provides
    public AuthRepository provideAuthRepository(FirebaseFirestore firestore, FirebaseAuth firebaseAuth, FirebaseStorage storage){
        return new AuthRepository(firestore, firebaseAuth, storage);
    }

    @Provides
    public MovieRepository provideMovieRepository(FirebaseFirestore firestore, FirebaseAuth firebaseAuth, ApiPoint apiPoint){
        return new MovieRepository(firestore, firebaseAuth, apiPoint);
    }
}
