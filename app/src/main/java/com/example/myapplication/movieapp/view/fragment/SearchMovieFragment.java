package com.example.myapplication.movieapp.view.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.myapplication.movieapp.R;
import com.example.myapplication.movieapp.model.remote.Movie;
import com.example.myapplication.movieapp.view.activity.MovieDetailActivity;
import com.example.myapplication.movieapp.view.adapter.RecyclerInterface;
import com.example.myapplication.movieapp.view.adapter.SearchAndFavouriteMovieAdapter;
import com.example.myapplication.movieapp.viewmodel.MovieViewModel;

import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class SearchMovieFragment extends Fragment implements RecyclerInterface {
    private MovieViewModel movieViewModel;
    private SearchView searchView;
    private List<Movie> foundMovies;
    private SearchAndFavouriteMovieAdapter searchAndFavouriteMovieAdapter;
    private RecyclerView recyclerViewSearchMovie;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        movieViewModel = new ViewModelProvider(this).get(MovieViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search_movie, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        searchView = view.findViewById(R.id.searchView);
        recyclerViewSearchMovie = view.findViewById(R.id.recyclerViewSearchMovie);
        getNowPlayingMovie();
        searchMovie();
    }

    private void searchMovie(){
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String title) {
                checkTitle(title);
                return true;
            }
        });
    }

    private void checkTitle(String title){
        if(!title.isEmpty()){
            getMovieByTitle(title);
        }else{
            getNowPlayingMovie();
        }
    }

    private void getMovieByTitle(String title){
        movieViewModel.searchFilm(title).observe(getViewLifecycleOwner(), movies -> {
            if(movies != null){
                initRecyclerView(movies);
            }else{
                Toast.makeText(getContext(), "Oops. Movie hasn't found", Toast.LENGTH_LONG).show();
                getNowPlayingMovie();
            }
        });
    }

    private void getNowPlayingMovie(){
        movieViewModel.getNowPlayingFilms().observe(getViewLifecycleOwner(), movies -> {
            if(movies != null){
                initRecyclerView(movies);
            }else{
                Toast.makeText(getContext(), "Error. Something wrong", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void initRecyclerView(List<Movie> movies){
        foundMovies = movies;
        searchAndFavouriteMovieAdapter = new SearchAndFavouriteMovieAdapter(movies, this);
        recyclerViewSearchMovie.setAdapter(searchAndFavouriteMovieAdapter);
        recyclerViewSearchMovie.setLayoutManager(new LinearLayoutManager(getContext()));
    }


    @Override
    public void onItemClick(int position) {
        Movie movie = foundMovies.get(position);
        Intent intent = new Intent(getActivity(), MovieDetailActivity.class);
        intent.putExtra("Movie", movie);
        startActivity(intent);
    }
}