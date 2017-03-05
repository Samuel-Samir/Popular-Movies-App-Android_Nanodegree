package com.example.android.popularmoviesapp;


import android.app.Activity;
import android.app.ProgressDialog;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import com.example.android.popularmoviesapp.Database.MovieContract;
import com.example.android.popularmoviesapp.Models.MovieDetails;
import com.example.android.popularmoviesapp.Models.MovieReviews;
import com.example.android.popularmoviesapp.Models.MoviesList;
import com.example.android.popularmoviesapp.Models.MovietTrailers;
import com.example.android.popularmoviesapp.Models.ReviewsList;
import com.example.android.popularmoviesapp.Models.TrailersList;

/**
 * Created by samuel on 3/5/2017.
 */

public class FetchFromDataBase extends AsyncTask<String ,Void,Object> {


    private final String LOG_TAG = FetchFromDataBase.class.getName();
    private Activity activity ;

    public  FetchFromDataBase  (Activity activity )
    {
        this.activity=activity;
    }


//////////////////////////////////////////////////////////////////////////

    public interface FetchFromDBCallback
    {
        public  void onPostExecute(Object object);
    }
    private  FetchFromDBCallback fetchFromDBCallback ;
    public  void setFetchMoviesDBCallback ( FetchFromDBCallback fetchFromDBCallback)
    {
        this.fetchFromDBCallback =fetchFromDBCallback;
    }
    ////////////////////////////////////////////////////////////////////////////


    @Override
    protected Object doInBackground(String... params) {


        if (params[0].equals("movie"))
        {
            MoviesList MoviesListDB ;
            Uri moviesUri = MovieContract.MovieEntry.CONTENT_URI;
            Cursor cursor = activity.getContentResolver().query(moviesUri , null , null , null , null);
            MoviesListDB = getMoviesFromCursor(cursor);
            cursor.close();
            return MoviesListDB;
        }
        else if (params[0].equals("videos"))
        {

            Uri trailersUri = Uri.parse(MovieContract.TrailerEntry.CONTENT_URI.toString() +"/"+params[1]);
            Cursor cursor = activity.getContentResolver().query(trailersUri , null , null , null , null);
            TrailersList movieVideoList = getTrailersFromCursor(cursor);
            cursor.close();
            return movieVideoList ;
        }
        else if (params[0].equals("reviews"))
        {
            Uri reviewWithIdUri =Uri.parse(MovieContract.ReviewEntry.CONTENT_URI.toString()+"/"+params[1]);
            Cursor cursor = activity.getContentResolver().query(reviewWithIdUri,null,null,null,null);
            ReviewsList movieReviewsList = getReviewFromCursor(cursor);
            cursor.close();
            return movieReviewsList;
        }


        return  null;
    }
    private MoviesList getMoviesFromCursor(Cursor cursor) {

        int movieIdIndex = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_ID);
        int movieTitleIndex = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_TITLE);
        int posterUrlIndex = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_POSTER_URL);
        int backdropUrlIndex = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_BAVKDROP_URL);
        int movieRatingIndex = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_RATING);
        int moviedescriptionIndex =  cursor.getColumnIndex("description");
        int movievoteCountIndex = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_VOTE_COUNT);
        int movieDateIndex = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_REALEASE_DATE);
        String names [] = cursor.getColumnNames() ;

        MoviesList favMoviesList = new MoviesList() ;
        while (cursor.moveToNext())
        {
            MovieDetails movieContent  =  new MovieDetails();
            movieContent.id = cursor.getInt(movieIdIndex) ;
            movieContent.original_title = cursor.getString(movieTitleIndex) ;
            movieContent.poster_path = cursor.getString(posterUrlIndex) ;
            movieContent.backdrop_path =  cursor.getString(backdropUrlIndex) ;
            movieContent.vote_average = cursor.getDouble(movieRatingIndex) ;
            movieContent.overview = cursor.getString(moviedescriptionIndex) ;
            movieContent.vote_count = cursor.getInt(movievoteCountIndex) ;
            movieContent.release_date =  cursor.getString(movieDateIndex) ;
            favMoviesList.results.add(movieContent);
        }
        cursor.close();


        return  favMoviesList ;
    }

    private ReviewsList getReviewFromCursor(Cursor cursor) {

        int reviewAuthorIdx = cursor.getColumnIndex(MovieContract.ReviewEntry.COLUMN_AUTHOR);
        int reviewContentIdx = cursor.getColumnIndex(MovieContract.ReviewEntry.COLUMN_CONTENT);
        ReviewsList movieReviewsList = new ReviewsList() ;
        while (cursor.moveToNext()){
            MovieReviews movieReviews = new MovieReviews() ;
            movieReviews.author = cursor.getString(reviewAuthorIdx) ;
            movieReviews.content =  cursor.getString(reviewContentIdx) ;
            movieReviewsList .results.add(movieReviews);
        }
        cursor.close();

        return movieReviewsList;
    }

    public  TrailersList getTrailersFromCursor(Cursor cursor) {

        int trailerKeyIndex = cursor.getColumnIndex(MovieContract.TrailerEntry.COLUMN_KEY);
        int trailerNameIndex = cursor.getColumnIndex(MovieContract.TrailerEntry.COLUMN_NAME);

        TrailersList movieVideoList = new TrailersList() ;
        while (cursor.moveToNext()){
            MovietTrailers video = new MovietTrailers() ;
            video.key = cursor.getString(trailerKeyIndex) ;
            video.name = cursor.getString(trailerNameIndex) ;
            movieVideoList.results.add(video) ;
        }
        cursor.close();

        return movieVideoList;
    }




    @Override
    protected void onPreExecute() {
        super.onPreExecute();    }

    @Override
    protected void onPostExecute(Object object) {
        if (fetchFromDBCallback !=null)
        {
            fetchFromDBCallback.onPostExecute(object);
        }
    }
}
