package com.example.myapplication.movieapp.di;

import com.example.myapplication.movieapp.model.remote.ApiPoint;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
@InstallIn(SingletonComponent.class)
public class ApiModule {

    @Singleton
    @Provides
    public ApiPoint provideRetrofit(){
        return new Retrofit.Builder()
                .baseUrl("https://api.themoviedb.org")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiPoint.class);
    }
}
