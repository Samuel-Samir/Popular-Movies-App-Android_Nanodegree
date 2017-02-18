package com.example.android.popularmoviesapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;

import com.example.android.popularmoviesapp.Models.MoviesList;
import com.example.android.popularmoviesapp.Utils.NetworkUtils;
import com.google.gson.Gson;

import java.io.IOException;
import java.net.URL;

/**
 * Created by samuel on 2/18/2017.
 */

public class FetchMoviesAsyncTask extends AsyncTask<String ,Void ,MoviesList> {

    private final String LOG_TAG = FetchMoviesAsyncTask.class.getName();
    private ProgressDialog dialog;

    public FetchMoviesAsyncTask (Activity activity)
    {
        dialog =new ProgressDialog(activity);
    }
    public  interface FetchMoviesAsyncTaskCallBack
    {
         void onPostExecute (MoviesList moviesList);
    }

    private FetchMoviesAsyncTaskCallBack fetchMoviesAsyncTaskCallBack;

    public void setFetchMoviesAsyncTaskCallBack (FetchMoviesAsyncTaskCallBack FetchMoviesAsyncTaskCallBack)
    {
        this.fetchMoviesAsyncTaskCallBack =FetchMoviesAsyncTaskCallBack;

    }


    @Override
    protected MoviesList doInBackground(String... params) {
        if (params.length==0)
        {
            return null;
        }
        String moviesOrder =params[0];
        URL moviesUrlRequest = NetworkUtils.buildUrl(moviesOrder);
        try {

            String jasonResponse =NetworkUtils.getResponseFromAPI(moviesUrlRequest);
            Gson gson =new Gson();
            MoviesList moviesListResponse ;
            moviesListResponse = gson.fromJson(jasonResponse ,MoviesList.class);
            return moviesListResponse;

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
    protected void onPostExecute(MoviesList moviesList) {
      //  super.onPostExecute(moviesList);
        if(moviesList!=null)
        {
            fetchMoviesAsyncTaskCallBack.onPostExecute(moviesList);
        }
        if (dialog.isShowing()) {
            dialog.dismiss();
        }


    }
}
