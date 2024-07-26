package com.example.myapplication.movieapp.view.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapplication.movieapp.R;
import com.example.myapplication.movieapp.model.remote.Movie;
import com.example.myapplication.movieapp.view.activity.MovieDetailActivity;
import com.example.myapplication.movieapp.view.adapter.HomeMovieAdapter;
import com.example.myapplication.movieapp.view.adapter.RecyclerViewInterface;
import com.example.myapplication.movieapp.viewmodel.MovieViewModel;

import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class HomeFragment extends Fragment implements RecyclerViewInterface {
    private MovieViewModel movieViewModel;
    private List<Movie> topRatedMovies, nowPlayingMovies;
    private RecyclerView recyclerViewTopRated, recyclerViewNowPlaying;
    private HomeMovieAdapter movieAdapterTopRated, movieAdapterNowPlaying;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        movieViewModel = new ViewModelProvider(this).get(MovieViewModel.class);

        //getNowPlayingMovies();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerViewTopRated = view.findViewById(R.id.recyclerViewTopRated);
        recyclerViewNowPlaying = view.findViewById(R.id.recyclerViewNowPlaying);
        getTopRatedMovies();
        getNowPlayingMovies();
        //printNowPlayingMovies();
        //initRecyclerViewTopRated();
        //initRecyclerViewNowPlaying();
    }


    private void getTopRatedMovies(){
        movieViewModel.getTopRatedFilms().observe(getViewLifecycleOwner(), movies -> {
            if(movies != null){
                initRecyclerViewTopRated(movies);
            }
        });
    }

    private void getNowPlayingMovies(){
        movieViewModel.getNowPlayingFilms().observe(getViewLifecycleOwner(), movies -> {
            if(movies != null){
                initRecyclerViewNowPlaying(movies);
            }
        });
    }

    private void initRecyclerViewTopRated(List<Movie> movies){
        topRatedMovies = movies;
        movieAdapterTopRated = new HomeMovieAdapter(movies, this, recyclerViewTopRated.getId());
        recyclerViewTopRated.setAdapter(movieAdapterTopRated);
        recyclerViewTopRated.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
    }

    private void initRecyclerViewNowPlaying(List<Movie> movies){
        nowPlayingMovies = movies;
        movieAdapterNowPlaying = new HomeMovieAdapter(movies, this, recyclerViewNowPlaying.getId());
        recyclerViewNowPlaying.setAdapter(movieAdapterNowPlaying);
        recyclerViewNowPlaying.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
    }

    private void goToMovieDetail(Movie movie){
        Intent intent = new Intent(getActivity(), MovieDetailActivity.class);
        intent.putExtra("Movie", movie);
        startActivity(intent);
    }

    @Override
    public void onItemClick(int recycler_id, int position) {
        Movie movie;
        if(recycler_id == recyclerViewTopRated.getId()){
            movie = topRatedMovies.get(position);
        }else{
            movie = nowPlayingMovies.get(position);
        }
        System.out.println(movie.getTitleMovie());
        goToMovieDetail(movie);
    }
}