package com.example.android.popularmoviesapp.Models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by samuel on 3/4/2017.
 */

public class MovieReviews implements Parcelable {

    public String author ;
    public String content;

    public MovieReviews ()
    {

    }
    protected MovieReviews(Parcel in) {
        author = in.readString();
        content = in.readString();
    }

    public static final Creator<MovieReviews> CREATOR = new Creator<MovieReviews>() {
        @Override
        public MovieReviews createFromParcel(Parcel in) {
            return new MovieReviews(in);
        }

        @Override
        public MovieReviews[] newArray(int size) {
            return new MovieReviews[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(author);
        dest.writeString(content);
    }
}
