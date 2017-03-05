package com.example.android.popularmoviesapp.Utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.provider.Settings;

import com.example.android.popularmoviesapp.BuildConfig;
import com.example.android.popularmoviesapp.R;

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

    public static String MoviesOreder ="popular";
    public static int OrderIndex =0;


    public static URL buildUrl (String moviesOrder , Context context )
    {
        Uri buildUri = Uri.parse(Movies_BASE_URL).buildUpon()
                .appendEncodedPath(moviesOrder)
                //.appendQueryParameter(API_KEY, BuildConfig.Movie_APP_API_KEY)
                .appendQueryParameter(API_KEY ,context.getString(R.string.MOVIE_DB_API))
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



    public static boolean checkInternetConnection(Context context)
    {

        ConnectivityManager con_manager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        if (con_manager.getActiveNetworkInfo() != null
                && con_manager.getActiveNetworkInfo().isAvailable()
                && con_manager.getActiveNetworkInfo().isConnected()) {
            return true;
        } else {
            return false;
        }
    }

    public static void connectionAlart (final Activity activity)
    {

        AlertDialog alertDialog =  new AlertDialog.Builder(activity)
                .setTitle(activity.getResources().getString(R.string.connection_failed))
                .setMessage(activity.getResources().getString(R.string.massage))

                .setNegativeButton(activity.getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                           activity.finish();
                    }
                })
                .show();
    }
}

