package com.example.android.popularmoviesapp.Models;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by samuel on 2/18/2017.
 */

public class MoviesList implements Parcelable{
    public List<MovieDetails> results =new ArrayList<MovieDetails>();
    public int page ;

    public  MoviesList ()
    {

    }
    protected MoviesList(Parcel in) {
        results = in.createTypedArrayList(MovieDetails.CREATOR);
        page = in.readInt();
    }

    public static final Creator<MoviesList> CREATOR = new Creator<MoviesList>() {
        @Override
        public MoviesList createFromParcel(Parcel in) {
            return new MoviesList(in);
        }

        @Override
        public MoviesList[] newArray(int size) {
            return new MoviesList[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(results);
        dest.writeInt(page);
    }
}
