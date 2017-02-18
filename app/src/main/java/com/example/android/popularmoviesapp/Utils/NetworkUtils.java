package com.example.android.popularmoviesapp.Utils;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;

import com.example.android.popularmoviesapp.BuildConfig;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by samuel on 2/18/2017.
 */

public final class NetworkUtils {

    private static final String TAG  = NetworkUtils.class.getName();
    private static final String Movies_BASE_URL ="http://api.themoviedb.org/3/movie/";
    private static final String API_KEY = "api_key";

    public static URL buildUrl (String moviesOrder )
    {
        Uri buildUri = Uri.parse(Movies_BASE_URL).buildUpon()
                .appendEncodedPath(moviesOrder)
                .appendQueryParameter(API_KEY, BuildConfig.Movie_APP_API_KEY)
                .build();
        URL url = null ;
        try {
            url = new URL(buildUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    public static String getResponseFromAPI (URL url) throws IOException
    {
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        try {

            InputStream inputStream = httpURLConnection.getInputStream();
            Scanner scanner = new Scanner(inputStream);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput)
                return scanner.next();
            else
                return null;
        }
        finally {
            httpURLConnection.disconnect();
        }


    }
}

