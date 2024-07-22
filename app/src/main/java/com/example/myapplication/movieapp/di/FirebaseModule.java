package com.example.myapplication.movieapp.di;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public class FirebaseModule {

    @Singleton
    @Provides
    public FirebaseFirestore provideFirebaseFirestore(){
        return FirebaseFirestore.getInstance();
    }

    @Singleton
    @Provides
    public FirebaseStorage provideFirebaseStorage(){
        return FirebaseStorage.getInstance();
    }

    @Singleton
    @Provides
    public FirebaseAuth provideFirebaseAuth(){
        return FirebaseAuth.getInstance();
    }

}
