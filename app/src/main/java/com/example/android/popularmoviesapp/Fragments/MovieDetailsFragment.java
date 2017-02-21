package com.example.android.popularmoviesapp.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.android.popularmoviesapp.Models.MovieDetails;
import com.example.android.popularmoviesapp.R;
import com.squareup.picasso.Picasso;


public class MovieDetailsFragment extends Fragment {
    private MovieDetails myMovieDetails ;

    private ImageView  photoImageView ;
    private TextView   nameTextView ;
    private TextView   dateTextView ;
    private TextView   overviewTextView;
    private TextView   ratingTextView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_movie_details, container, false);
        getActivity().setTitle(getResources().getString(R.string.movie_details));
        if(savedInstanceState==null)
        {
            Bundle arguments = getArguments();
            myMovieDetails = (MovieDetails) arguments.getParcelable("myMovieDetails");
        }
        else if(savedInstanceState!=null)
        {
            myMovieDetails = (MovieDetails) savedInstanceState.getParcelable("myMovie");
        }
        if (myMovieDetails!=null)
        {
            setViews(rootView);
        }
        return rootView ;
    }

    public void setViews (View rootView)
    {
        photoImageView =(ImageView) rootView.findViewById(R.id.moviePhoto);
        nameTextView =(TextView) rootView.findViewById(R.id.movie_name);
        dateTextView =(TextView) rootView.findViewById(R.id.date);
        ratingTextView = (TextView) rootView.findViewById(R.id.rate);
        overviewTextView =(TextView) rootView.findViewById(R.id.overview);

        nameTextView.setText(myMovieDetails.original_title);
        dateTextView.setText(myMovieDetails.release_date);
        String stringrate = String.valueOf(myMovieDetails.vote_average)+"/10";
        ratingTextView.setText(stringrate);
        overviewTextView.setText(myMovieDetails.overview);

        String baseimagUrl ="http://image.tmdb.org/t/p/w320/";
        String imagUrl = myMovieDetails.poster_path;
        Picasso.with(getActivity())
                .load(baseimagUrl+imagUrl)
                .placeholder(R.drawable.holder)
                .error(R.drawable.noposter)
                .into(photoImageView);
    }


    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("myMovie" ,myMovieDetails);
    }

}
