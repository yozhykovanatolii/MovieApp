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
import com.example.myapplication.movieapp.view.adapter.RecyclerInterface;
import com.example.myapplication.movieapp.view.adapter.SearchAndFavouriteMovieAdapter;
import com.example.myapplication.movieapp.viewmodel.MovieViewModel;

import java.util.ArrayList;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class FavouriteMoviesFragment extends Fragment implements RecyclerInterface {
    private MovieViewModel movieViewModel;
    private SearchAndFavouriteMovieAdapter searchAndFavouriteMovieAdapter;
    private RecyclerView recyclerViewFavouriteMovies;
    private ArrayList<Movie> favouriteMovies;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        movieViewModel = new ViewModelProvider(this).get(MovieViewModel.class);
        favouriteMovies = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_favourite_movies, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerViewFavouriteMovies = view.findViewById(R.id.recyclerViewFavouriteMovies);
        getFavouriteMovies();
    }

    private void getFavouriteMovies(){
        movieViewModel.getUserFavouriteFilms().observe(getViewLifecycleOwner(), movies -> {
            if(movies != null){
                getMovieById(movies);
            }
        });
    }

    private void getMovieById(List<Integer> movies){
        for(Integer id: movies){
            addMovieIntoList(id, movies.size());
        }
    }

    private void addMovieIntoList(int movie_id, int size){
        movieViewModel.findFilmById(movie_id).observe(getViewLifecycleOwner(), movie -> {
            if(movie != null){
                favouriteMovies.add(movie);
            }
            if(favouriteMovies.size() == size){
                initRecyclerView();
            }
        });
    }

    private void initRecyclerView(){
        searchAndFavouriteMovieAdapter = new SearchAndFavouriteMovieAdapter(favouriteMovies, this);
        recyclerViewFavouriteMovies.setAdapter(searchAndFavouriteMovieAdapter);
        recyclerViewFavouriteMovies.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    @Override
    public void onItemClick(int position) {
        Movie movie = favouriteMovies.get(position);
        Intent intent = new Intent(getActivity(), MovieDetailActivity.class);
        intent.putExtra("Movie", movie);
        startActivity(intent);
    }
}