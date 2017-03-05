package com.example.android.popularmoviesapp.Adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.provider.Settings;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.android.popularmoviesapp.Models.MovieReviews;
import com.example.android.popularmoviesapp.Models.MovietTrailers;
import com.example.android.popularmoviesapp.Models.ReviewsList;
import com.example.android.popularmoviesapp.Models.TrailersList;
import com.example.android.popularmoviesapp.R;

/**
 * Created by samuel on 3/4/2017.
 */

public class TrailersAdapter extends  RecyclerView.Adapter <TrailersAdapter.RecyclerViewTrailersAdapterHolder>{

    private TrailersList trailersList ;
    private ReviewsList reviewsList ;
    private Activity myActivity;
    private boolean isVideo  ;

    public void setTrailersList (Object object  , Activity myActivity , boolean isVideo)
    {
        this.isVideo =isVideo;
        if (isVideo==true)
            this.trailersList = (TrailersList) object ;
        if (isVideo==false)
            this.reviewsList = (ReviewsList) object;
        this.myActivity = myActivity ;
        notifyDataSetChanged();
    }


    @Override
    public RecyclerViewTrailersAdapterHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutMovieListItem =R.layout.trailers_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;
        View view = inflater.inflate(layoutMovieListItem ,parent ,shouldAttachToParentImmediately);
        return new RecyclerViewTrailersAdapterHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerViewTrailersAdapterHolder holder, int position) {
        if (isVideo==true)
        {
            final MovietTrailers movietTrailer = trailersList.results.get(position);
            holder.trailerTextView.setText(movietTrailer.name);
            holder.linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isVideo==true) {
                        String  videoUri = "https://www.youtube.com/watch?v="+movietTrailer.key;
                        Intent youtubeIntent = new Intent(Intent.ACTION_VIEW , Uri.parse(videoUri));
                        myActivity.startActivity(youtubeIntent);
                    }
                }
            });
        }
        else if (isVideo==false)
        {
            holder.imageView.setVisibility(View.INVISIBLE);
            final MovieReviews movieReviews = reviewsList.results.get(position);
            holder.trailerTextView.setText(movieReviews.author);

            holder.linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    new AlertDialog.Builder(myActivity)
                            .setTitle(movieReviews.author)
                            .setMessage(movieReviews.content)
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            })
                            .show();

                }
            });

        }


    }

    @Override
    public int getItemCount() {

        if (trailersList!=null && isVideo==true)
        {
            return trailersList.results.size();
        }

        if(reviewsList!=null && isVideo==false)
        {
            return reviewsList.results.size();
        }
        return 0;
    }

    public class RecyclerViewTrailersAdapterHolder extends RecyclerView.ViewHolder
    {
        private TextView trailerTextView ;
        private LinearLayout linearLayout ;
        private ImageView imageView;

        public RecyclerViewTrailersAdapterHolder(View itemView) {
            super(itemView);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.trailers_layout);
            trailerTextView= (TextView) itemView.findViewById(R.id.trailers_name);
            imageView =(ImageView) itemView.findViewById(R.id.playView);
        }
    }
}
