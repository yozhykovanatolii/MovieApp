package com.example.myapplication.movieapp.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.movieapp.R;
import com.example.myapplication.movieapp.model.remote.Movie;
import com.example.myapplication.movieapp.util.TextConverterUtil;
import com.squareup.picasso.Picasso;

import java.util.List;

public class HomeMovieAdapter extends RecyclerView.Adapter<HomeMovieAdapter.ViewHolder> {
    private List<Movie> movies;
    private TextConverterUtil converter;
    private RecyclerViewInterface recyclerViewInterface;
    private int recycler_id;

    public HomeMovieAdapter(List<Movie> movies, RecyclerViewInterface recyclerViewInterface, int recycler_id){
        this.movies = movies;
        this.recyclerViewInterface = recyclerViewInterface;
        this.recycler_id = recycler_id;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_list, parent, false);
        return new ViewHolder(view, recyclerViewInterface, recycler_id);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Movie movie = movies.get(position);
        converter = new TextConverterUtil();
        Picasso.get().load("https://image.tmdb.org/t/p/w200" + movie.getPosterPhoto()).into(holder.poster);
        holder.titleText.setText(converter.getText(movie.getTitleMovie()));
        holder.ratingText.setText(String.format("%.1f", movie.getReview_average()));
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView poster;
        private TextView titleText, ratingText;

        public ViewHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface, int recycler_id) {
            super(itemView);
            poster = itemView.findViewById(R.id.poster);
            titleText = itemView.findViewById(R.id.titleText);
            ratingText = itemView.findViewById(R.id.ratingText);

            itemView.setOnClickListener(view -> {
                if(recyclerViewInterface != null){
                    int position = getAdapterPosition();

                    if(position != RecyclerView.NO_POSITION){
                        recyclerViewInterface.onItemClick(recycler_id, position);
                    }
                }
            });
        }
    }
}
