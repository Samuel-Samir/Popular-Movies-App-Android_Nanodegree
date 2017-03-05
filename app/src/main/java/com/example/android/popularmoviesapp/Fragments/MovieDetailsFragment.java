package com.example.android.popularmoviesapp.Fragments;


import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.popularmoviesapp.Adapters.RecyclerViewAdapter;
import com.example.android.popularmoviesapp.Adapters.TrailersAdapter;
import com.example.android.popularmoviesapp.Database.MovieContract;
import com.example.android.popularmoviesapp.FetchFromDataBase;
import com.example.android.popularmoviesapp.FetchMoviesAsyncTask;
import com.example.android.popularmoviesapp.Models.MovieDetails;
import com.example.android.popularmoviesapp.Models.MoviesList;
import com.example.android.popularmoviesapp.Models.ReviewsList;
import com.example.android.popularmoviesapp.Models.TrailersList;
import com.example.android.popularmoviesapp.R;
import com.example.android.popularmoviesapp.Utils.NetworkUtils;
import com.squareup.picasso.Picasso;


public class MovieDetailsFragment extends Fragment {
    private MovieDetails myMovieDetails ;

    private TrailersList trailersList ;
    private TrailersAdapter trailersAdapter ;
    private RecyclerView trailersRecyclerView ;

    private ReviewsList reviewsList ;
    private TrailersAdapter reviewsAdapter ;
    private RecyclerView reviewsRecyclerView;

    private ImageView  photoImageView ;
    private TextView   nameTextView ;
    private TextView   dateTextView ;
    private TextView   overviewTextView;
    private TextView   ratingTextView;
    private TextView noComments ;
    private TextView noVides ;
    private String officalVideoKey ;
    private Button addToFavoritButton ;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_movie_details, container, false);
        setHasOptionsMenu(true);
        getActivity().setTitle(getResources().getString(R.string.movie_details));
        if(savedInstanceState==null)
        {
            Bundle arguments = getArguments();
            myMovieDetails = (MovieDetails) arguments.getParcelable("myMovieDetails");
            if (myMovieDetails!=null)
            {
                setViews(rootView);
                getTrailers ();
                getReviews ();

            }
        }
        else if(savedInstanceState!=null) {
            myMovieDetails = (MovieDetails) savedInstanceState.getParcelable("myMovie");
            reviewsList = (ReviewsList) savedInstanceState.getParcelable("movieReviewsList");
            trailersList = (TrailersList) savedInstanceState.getParcelable("movieTrailersList");
            if (myMovieDetails != null) {
                setViews(rootView);
                trailersAdapter.setTrailersList(trailersList,getActivity() ,true);
                if(trailersList.results.size()==0)
                    noVides.setText(getResources().getString(R.string.no_trailers));

                reviewsAdapter.setTrailersList(reviewsList,getActivity() , false);
                if(reviewsList.results.size()==0)
                    noComments.setText(getResources().getString(R.string.no_reviews));
            }

        }

        addToFavoritButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onFavoriteClick();
            }
        });
        return rootView ;
    }

    public void setViews (View rootView)
    {
        photoImageView =(ImageView) rootView.findViewById(R.id.moviePhoto);
        nameTextView =(TextView) rootView.findViewById(R.id.movie_name);
        dateTextView =(TextView) rootView.findViewById(R.id.date);
        ratingTextView = (TextView) rootView.findViewById(R.id.rate);
        overviewTextView =(TextView) rootView.findViewById(R.id.overview);
        noComments= (TextView) rootView.findViewById(R.id.no_comment);
        noVides =(TextView) rootView.findViewById(R.id.no_video) ;
        addToFavoritButton= (Button) rootView.findViewById(R.id.add_to_favorites);

        trailersRecyclerView = (RecyclerView) rootView.findViewById(R.id.trailers_recyclerView);
        trailersRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(),1 ));
        trailersAdapter =new TrailersAdapter() ;
        trailersRecyclerView.setAdapter(trailersAdapter);

        reviewsRecyclerView = (RecyclerView) rootView.findViewById(R.id.comments_recyclerView);
        reviewsRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(),1 ));
        reviewsAdapter =new TrailersAdapter() ;
        reviewsRecyclerView.setAdapter(reviewsAdapter);

        if (checkInsertedInDatabase( myMovieDetails.id))
        {
            addToFavoritButton.setText(getResources().getString(R.string.remove_from_favorites));
        }
        else{
            addToFavoritButton.setText(getResources().getString(R.string.add_to_favorites));
        }

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

    public void getTrailers ()
    {
        if(NetworkUtils.OrderIndex==2)
        {
            getTrailersFromDB() ;
        }
        else {
            String url =String.valueOf(myMovieDetails.id)+"/videos";

            FetchMoviesAsyncTask fetchMoviesAsyncTask =new FetchMoviesAsyncTask(getActivity()) ;
            fetchMoviesAsyncTask.setFetchMoviesAsyncTaskCallBack(new FetchMoviesAsyncTask.FetchMoviesAsyncTaskCallBack() {
                @Override
                public void onPostExecute(Object Object) {
                    trailersList =  (TrailersList) Object ;
                    trailersAdapter.setTrailersList(trailersList,getActivity() ,true);
                    if(trailersList.results.size()==0)
                    {
                        noVides.setText(getResources().getString(R.string.no_trailers));
                        officalVideoKey = "";

                    }
                    else
                        officalVideoKey = trailersList.results.get(0).key;

                }
            });

            fetchMoviesAsyncTask.execute(url , "trailers");
        }

    }

    private void getTrailersFromDB() {

        FetchFromDataBase fetchFromDataBase = new FetchFromDataBase(getActivity());
        fetchFromDataBase.setFetchMoviesDBCallback(new FetchFromDataBase.FetchFromDBCallback() {
            @Override
            public void onPostExecute(Object Object) {

                trailersList =  (TrailersList) Object ;
                trailersAdapter.setTrailersList(trailersList,getActivity() ,true);
                if(trailersList.results.size()==0)
                {
                    noVides.setText(getResources().getString(R.string.no_trailers));
                    officalVideoKey = "";

                }
                else
                    officalVideoKey = trailersList.results.get(0).key;
            }
        });
        fetchFromDataBase.execute("videos" ,String.valueOf(myMovieDetails.id )) ;
    }

    public void getReviews ()
    {
        if(NetworkUtils.OrderIndex==2)
        {
            getReviewsFromDB() ;
        }
        else {
            String uri =String.valueOf(myMovieDetails.id)+"/reviews";

            FetchMoviesAsyncTask fetchMoviesAsyncTask =new FetchMoviesAsyncTask(getActivity()) ;
            fetchMoviesAsyncTask.setFetchMoviesAsyncTaskCallBack(new FetchMoviesAsyncTask.FetchMoviesAsyncTaskCallBack() {
                @Override
                public void onPostExecute(Object Object) {
                    reviewsList =  (ReviewsList) Object ;
                    reviewsAdapter.setTrailersList(reviewsList,getActivity() , false);

                    if(reviewsList.results.size()==0)
                    {
                        noComments.setText(getResources().getString(R.string.no_reviews));
                    }
                }
            });

            fetchMoviesAsyncTask.execute(uri , "reviews");
        }
    }




    private void getReviewsFromDB() {
        FetchFromDataBase fetchFromDataBase = new FetchFromDataBase(getActivity());
        fetchFromDataBase.setFetchMoviesDBCallback(new FetchFromDataBase.FetchFromDBCallback() {
            @Override
            public void onPostExecute(Object Object) {
                reviewsList =  (ReviewsList) Object ;
                reviewsAdapter.setTrailersList(reviewsList,getActivity() , false);

                if(reviewsList.results.size()==0)
                {
                    noComments.setText(getResources().getString(R.string.no_reviews));
                }
            }
        });
        fetchFromDataBase.execute("reviews" ,String.valueOf(myMovieDetails.id ) ) ;
    }


    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("myMovie" ,myMovieDetails);
        outState.putParcelable("movieTrailersList" ,trailersList);
        outState.putParcelable("movieReviewsList" ,reviewsList);
    }

    private void shareVieoUri(String key )
    {
        String movieName = "Movie Name : " + myMovieDetails.original_title  ;
        String url = "https://www.youtube.com/watch?v="+ key;
        String sharedString = "#Movie_App\n"+movieName+"\nMovie Official Trailers : "+url+"\n" ;
        String title = " ";
        String mimeType = "text/plain";
        ShareCompat.IntentBuilder
                .from(getActivity())
                .setType(mimeType)
                .setText(sharedString)
                .startChooser();

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.share_menu, menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId() ;
        if (id==R.id.action_share)
        {
            shareVieoUri(officalVideoKey);
        }
        return super.onOptionsItemSelected(item);
    }


    public void onFavoriteClick()
    {
        if (addToFavoritButton.getText().toString().equals( getResources().getString(R.string.add_to_favorites))) {
            ContentValues reviewValues[] = new ContentValues[reviewsList.results.size()];
            for (int i = 0; i < reviewsList.results.size(); i++) {
                ContentValues tempVal = new ContentValues();
                tempVal.put(MovieContract.ReviewEntry.COLUMN_AUTHOR, reviewsList.results.get(i).author);
                tempVal.put(MovieContract.ReviewEntry.COLUMN_CONTENT, reviewsList.results.get(i).content);
                tempVal.put(MovieContract.ReviewEntry.COLUMN_MOVIE_ID, reviewsList.id);
                reviewValues[i] = tempVal;
            }
            ContentValues trailersValues[] = new ContentValues[trailersList.results.size()];
            for (int i = 0; i < trailersList.results.size(); i++) {
                ContentValues tempVal = new ContentValues();
                tempVal.put(MovieContract.TrailerEntry.COLUMN_MOVIE_ID, trailersList.id);
                tempVal.put(MovieContract.TrailerEntry.COLUMN_KEY, trailersList.results.get(i).key);
                tempVal.put(MovieContract.TrailerEntry.COLUMN_NAME, trailersList.results.get(i).name);

                trailersValues[i] = tempVal;
            }
            ContentValues movieVals = new ContentValues();
            movieVals.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID, myMovieDetails.id);
            movieVals.put(MovieContract.MovieEntry.COLUMN_TITLE, myMovieDetails.original_title);
            movieVals.put(MovieContract.MovieEntry.COLUMN_DESCRIPTION, myMovieDetails.overview);
            movieVals.put(MovieContract.MovieEntry.COLUMN_POSTER_URL, myMovieDetails.poster_path);
            movieVals.put(MovieContract.MovieEntry.COLUMN_RATING, myMovieDetails.vote_average);
            movieVals.put(MovieContract.MovieEntry.COLUMN_REALEASE_DATE, myMovieDetails.release_date);
            movieVals.put(MovieContract.MovieEntry.COLUMN_VOTE_COUNT, myMovieDetails.vote_count);
            movieVals.put(MovieContract.MovieEntry.COLUMN_BAVKDROP_URL, myMovieDetails.backdrop_path);

            Uri returnUriMovie = getActivity().getContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI, movieVals);
            int returnTrailerInserted = 0, returnReviewInserted = 0;
            if (trailersValues.length != 0) {
                returnTrailerInserted = getActivity().getContentResolver().bulkInsert(MovieContract.TrailerEntry.CONTENT_URI,
                        trailersValues);
            }
            if (reviewValues.length != 0) {
                returnReviewInserted = getActivity().getContentResolver().bulkInsert(MovieContract.ReviewEntry.CONTENT_URI,
                        reviewValues);
            }
            if (returnUriMovie != null) {
                addToFavoritButton.setText(getResources().getString(R.string.remove_from_favorites));
                Toast.makeText(getActivity() ,getResources().getString(R.string.add_toast) ,Toast.LENGTH_SHORT).show();
            }
        }
        else {
            int trailersDeleted = getActivity().getContentResolver().delete(MovieContract.TrailerEntry.CONTENT_URI ,
                    MovieContract.TrailerEntry.COLUMN_MOVIE_ID +" = ? " ,new String[]{String.valueOf(myMovieDetails.id)} );
            int reviewDeleted =  getActivity().getContentResolver().delete(MovieContract.ReviewEntry.CONTENT_URI ,
                    MovieContract.ReviewEntry.COLUMN_MOVIE_ID +" = ? " ,new String[]{String.valueOf(myMovieDetails.id)} );
            int moviesDeleted =getActivity().getContentResolver().delete(MovieContract.MovieEntry.CONTENT_URI ,
                    MovieContract.MovieEntry.COLUMN_MOVIE_ID+" = ? ",new String[]{String.valueOf(myMovieDetails.id)} );
            if(moviesDeleted!=0){
                addToFavoritButton.setText(getResources().getString(R.string.add_to_favorites));
                Toast.makeText(getActivity() ,getResources().getString(R.string.remove_toast) ,Toast.LENGTH_SHORT).show();


            }
        }

    }

    public  boolean checkInsertedInDatabase( int movieId)
    {
        Uri moviesWithIdUri =Uri.parse(MovieContract.MovieEntry.CONTENT_URI.toString());
        Cursor movieListCursor = getActivity().getContentResolver().query(moviesWithIdUri,null,null,null,null);
        int movieIdIndex =movieListCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_ID);
        while(movieListCursor.moveToNext()){
            int indexIterator = movieListCursor.getInt(movieIdIndex);
            if(indexIterator == movieId){
                return true;
            }
        }
        return false;
    }
}


