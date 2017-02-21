package com.example.android.popularmoviesapp.Adapters;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.android.popularmoviesapp.Fragments.MovieDetailsFragment;
import com.example.android.popularmoviesapp.Models.MovieDetails;
import com.example.android.popularmoviesapp.Models.MoviesList;
import com.example.android.popularmoviesapp.R;
import com.squareup.picasso.Picasso;

/**
 * Created by samuel on 2/18/2017.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter <RecyclerViewAdapter.RecyclerViewAdapterHolder> {
    private MoviesList moviesList ;
    private Activity myActivity;

    public void setMoviesList (MoviesList moviesList ,Activity myActivity )
    {
        this.myActivity = myActivity;
        this.moviesList = moviesList;
        notifyDataSetChanged();
    }
    @Override
    public RecyclerViewAdapterHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutMovieListItem =R.layout.movie_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;
        View view = inflater.inflate(layoutMovieListItem ,parent ,shouldAttachToParentImmediately);
        return new RecyclerViewAdapterHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerViewAdapterHolder holder, int position) {
        final MovieDetails movieDetails = moviesList.results.get(position);
        String baseimagUrl ="http://image.tmdb.org/t/p/w342/";
        String imagUrl = movieDetails.poster_path;
        final String fullImageUrl = baseimagUrl+imagUrl;
        Picasso.with(myActivity)
                .load(fullImageUrl)
                .placeholder(R.drawable.holder)
                .error(R.drawable.noposter)
                .into(holder.imageView);

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MovieDetailsFragment movieDetailsFragment = new MovieDetailsFragment();
                Bundle bundle = new Bundle();
                bundle.putParcelable("myMovieDetails", movieDetails);
                movieDetailsFragment.setArguments(bundle);
                ((FragmentActivity)myActivity).getSupportFragmentManager()
                        .beginTransaction().addToBackStack("movieDetailsFragment").replace(R.id.container,movieDetailsFragment).commit();


            }
        });
    }

    @Override
    public int getItemCount() {
        if (moviesList!=null)
        {
            return moviesList.results.size();
        }
        return 0;
    }

    public class RecyclerViewAdapterHolder extends RecyclerView.ViewHolder
    {
        private ImageView imageView ;

        public RecyclerViewAdapterHolder(View itemView) {
            super(itemView);
            imageView= (ImageView) itemView.findViewById(R.id.image_item);
        }
    }
}
