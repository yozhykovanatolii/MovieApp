package com.example.myapplication.movieapp.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.movieapp.R;
import com.example.myapplication.movieapp.model.remote.Movie;
import com.example.myapplication.movieapp.util.TextConverterUtil;
import com.google.android.material.imageview.ShapeableImageView;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SearchAndFavouriteMovieAdapter extends RecyclerView.Adapter<SearchAndFavouriteMovieAdapter.Holder> {
    private List<Movie> movieList;
    private RecyclerInterface recyclerInterface;

    public SearchAndFavouriteMovieAdapter(List<Movie> movieList, RecyclerInterface recyclerInterface){
        this.movieList = movieList;
        this.recyclerInterface = recyclerInterface;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movies, parent, false);
        return new Holder(view, recyclerInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        Movie movie = movieList.get(position);
        Picasso.get().load("https://image.tmdb.org/t/p/w500" + movie.getPosterPhoto()).into(holder.moviePoster);
        holder.originalTitle.setText(movie.getTitleMovie());
        holder.reviewAverageText.setText(String.format("%.1f", movie.getReview_average()));
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    public static class Holder extends RecyclerView.ViewHolder{
        private ShapeableImageView moviePoster;
        private TextView originalTitle, reviewAverageText;

        public Holder(@NonNull View itemView, RecyclerInterface recyclerInterface) {
            super(itemView);
            moviePoster = itemView.findViewById(R.id.moviePoster);
            originalTitle = itemView.findViewById(R.id.originalTitle);
            reviewAverageText = itemView.findViewById(R.id.reviewAverageText);

            itemView.setOnClickListener(view -> {
                if(recyclerInterface != null){
                    int position = getAdapterPosition();

                    if(position != RecyclerView.NO_POSITION){
                        recyclerInterface.onItemClick(position);
                    }
                }
            });
        }
    }
}
