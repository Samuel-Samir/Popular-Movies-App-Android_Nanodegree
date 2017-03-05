package com.example.android.popularmoviesapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.example.android.popularmoviesapp.Models.MoviesList;
import com.example.android.popularmoviesapp.Models.ReviewsList;
import com.example.android.popularmoviesapp.Models.TrailersList;
import com.example.android.popularmoviesapp.Utils.NetworkUtils;
import com.google.gson.Gson;

import java.io.IOException;
import java.net.URL;

/**
 * Created by samuel on 2/18/2017.
 */

public class FetchMoviesAsyncTask extends AsyncTask<String ,Void ,Object> {

    private final String LOG_TAG = FetchMoviesAsyncTask.class.getName();
    private ProgressDialog dialog;
    private Context context;

    public FetchMoviesAsyncTask (Activity activity)
    {
        dialog =new ProgressDialog(activity);
        this.context = activity ;
    }



    public  interface FetchMoviesAsyncTaskCallBack
    {
         void onPostExecute (Object Object);
    }

    private FetchMoviesAsyncTaskCallBack fetchMoviesAsyncTaskCallBack;

    public void setFetchMoviesAsyncTaskCallBack (FetchMoviesAsyncTaskCallBack FetchMoviesAsyncTaskCallBack)
    {
        this.fetchMoviesAsyncTaskCallBack =FetchMoviesAsyncTaskCallBack;

    }


    @Override
    protected Object doInBackground(String... params) {
        if (params.length==0)
        {
            return null;
        }
        String moviesOrder =params[0];
        URL moviesUrlRequest = NetworkUtils.buildUrl(moviesOrder , context );
        try {

            String jasonResponse =NetworkUtils.getResponseFromAPI(moviesUrlRequest);
            Gson gson =new Gson();
            if (params.length==1)
            {
                MoviesList moviesListResponse ;
                moviesListResponse = gson.fromJson(jasonResponse ,MoviesList.class);
                return moviesListResponse;
            }

            else if (params.length==2 && params[1].equals("trailers"))
            {
                TrailersList trailersList ;
                trailersList = gson.fromJson(jasonResponse,TrailersList.class);
                return trailersList ;
            }

            else if (params.length==2 && params[1].equals("reviews"))
            {
                ReviewsList reviewsList ;
                reviewsList = gson.fromJson(jasonResponse,ReviewsList.class);
                return reviewsList;
            }

            return null;

        } catch (IOException e) {
            e.printStackTrace();
            return null;

        }
    }


    @Override
    protected void onPreExecute() {
        this.dialog.setMessage("loading...");
        this.dialog.show();
        super.onPreExecute();
    }


    @Override
    protected void onPostExecute(Object Object) {
      //  super.onPostExecute(moviesList);
        if(Object!=null)
        {
            fetchMoviesAsyncTaskCallBack.onPostExecute(Object);
        }
        if (dialog.isShowing()) {
            dialog.dismiss();
        }


    }
}
